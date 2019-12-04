package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;
import java.util.Set;

public class ValidateMoveController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

        //if game has ended, tell user he's won
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        if (game.isHasGameEnded()) {
            Message message = new Message("Your opponent has already resigned, please click submit/resign to win", MessageType.info);
            return new Gson().toJson(message, Message.class);
        }

        //if game is on
        Message message = null;
        boolean isValidMove = false;

        if (request.body().contains("{}")) {
            message = new Message("You have moved to the same position!!!", MessageType.error);
            return new Gson().toJson(message, Message.class);
        }

        Move move = new Gson().fromJson(request.body(), Move.class);


        Board board = game.getBoard();
        if (board.getSquarePieceIdMap().get(move.getEnd()) != null) {
            message = new Message("Square already occupied", MessageType.error);
            return new Gson().toJson(message, Message.class);
        }

        Piece pieceMoved = board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).getPiece();
        if (pieceMoved.getType().equals(PieceType.SINGLE)) {
            isValidMove = validateSingleMove(move, pieceMoved.getPieceId());
        } else {
            isValidMove = validateKingMove(move);
        }

        boolean isJump = false;
        if (!isValidMove) {
            isJump = validateForwardJump(move, pieceMoved.getPieceId(), board);
            if (!isJump && pieceMoved.getType().equals(PieceType.KING)) {
                isJump = validateBackJump(move, pieceMoved.getPieceId(), board);
            }
            isValidMove = isJump;
        }

        if (!isJump && isValidMove) {
            if (isAnyJumpAvailable(board.getSquarePieceIdMap(), pieceMoved, board.getKingPieces())) {
                isValidMove = false;
                message = new Message("Invalid Move! You have to take the available jump!!!", MessageType.error);
            }
        }

        if (isValidMove) {
            checkIfPieceMovedBecameKing(move.getEnd().getRow(), pieceMoved, board);
            board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).setPiece(null);
            board.getRows().get(move.getEnd().getRow()).getSquares().get(move.getEnd().getCell()).setPiece(pieceMoved);
            board.getSquarePieceIdMap().remove(move.getStart());
            board.getSquarePieceIdMap().put(move.getEnd(), pieceMoved.getPieceId());
            //check if all opponent peices are null
            amIWinning(game, board, userName);
            if (game.isEasy() && isJump && isJumpAvailable(move.getEnd(), pieceMoved, board.getSquarePieceIdMap(), pieceMoved.getType().equals(PieceType.KING))) {
                message = new Message("Valid move. You still have opponent piece to conquer", MessageType.jump);
            } else {
                message = new Message("Valid move. Hit Submit move!!!", MessageType.info);
            }
        } else if (null == message) {
            message = new Message("Invalid Move!!!", MessageType.error);
        }
        return new Gson().toJson(message, Message.class);
    }

    private boolean isAnyJumpAvailable(Map<Position, Integer> squarePieceIdMap, Piece pieceMoved, Set<Integer> kingPieces) {
        boolean isAnyJumpAvailable = false;
        for (Position position : squarePieceIdMap.keySet()) {
            if (null != squarePieceIdMap.get(position) && squarePieceIdMap.get(position) < 12 && pieceMoved.getPieceId() < 12) {
                isAnyJumpAvailable = isJumpAvailable(position, pieceMoved, squarePieceIdMap, kingPieces.contains(squarePieceIdMap.get(position)));
                if (isAnyJumpAvailable) {
                    return isAnyJumpAvailable;
                }
            } else if (null != squarePieceIdMap.get(position) && squarePieceIdMap.get(position) >= 12 && pieceMoved.getPieceId() >= 12) {
                isAnyJumpAvailable = isJumpAvailable(position, pieceMoved, squarePieceIdMap, kingPieces.contains(squarePieceIdMap.get(position)));
                if (isAnyJumpAvailable) {
                    return isAnyJumpAvailable;
                }
            }
        }
        return isAnyJumpAvailable;
    }

    private boolean isJumpAvailable(Position end, Piece pieceMoved, Map<Position, Integer> squarePieceIdMap, boolean isKing) {
        int row = end.getRow();
        int col = end.getCell();
        boolean moreJumpAvailable = false;
        if (pieceMoved.getPieceId() < 12) {
            Position drtj = new Position(row + 2, col + 2);
            Position drt = new Position(row + 1, col + 1);
            if (row < 6 && col < 6 && squarePieceIdMap.get(drt) != null && squarePieceIdMap.get(drt) >= 12 && squarePieceIdMap.get(drtj) == null) {
                moreJumpAvailable = true;
            }
            if (!moreJumpAvailable) {
                Position dltj = new Position(row + 2, col - 2);
                Position dlt = new Position(row + 1, col - 1);
                if (row < 6 && col > 1 && squarePieceIdMap.get(dlt) != null && squarePieceIdMap.get(dlt) >= 12 && squarePieceIdMap.get(dltj) == null) {
                    moreJumpAvailable = true;
                }
            }
        } else {
            Position drtj = new Position(row - 2, col + 2);
            Position drt = new Position(row - 1, col + 1);
            if (row > 1 && col < 6 && squarePieceIdMap.get(drt) != null && squarePieceIdMap.get(drt) < 12 && squarePieceIdMap.get(drtj) == null) {
                moreJumpAvailable = true;
            }
            if (!moreJumpAvailable) {
                Position dltj = new Position(row - 2, col - 2);
                Position dlt = new Position(row - 1, col - 1);
                if (row > 1 && col > 1 && squarePieceIdMap.get(dlt) != null && squarePieceIdMap.get(dlt) < 12 && squarePieceIdMap.get(dltj) == null) {
                    moreJumpAvailable = true;
                }
            }
        }
        if (isKing && !moreJumpAvailable) {
            if (pieceMoved.getPieceId() >= 12) {
                Position drtj = new Position(row + 2, col + 2);
                Position drt = new Position(row + 1, col + 1);
                if (row < 6 && col < 6 && squarePieceIdMap.get(drt) != null && squarePieceIdMap.get(drt) < 12 && squarePieceIdMap.get(drtj) == null) {
                    moreJumpAvailable = true;
                }
                if (!moreJumpAvailable) {
                    Position dltj = new Position(row + 2, col - 2);
                    Position dlt = new Position(row + 1, col - 1);
                    if (row < 6 && col > 1 && squarePieceIdMap.get(dlt) != null && squarePieceIdMap.get(dlt) < 12 && squarePieceIdMap.get(dltj) == null) {
                        moreJumpAvailable = true;
                    }
                }
            } else {
                Position drtj = new Position(row - 2, col + 2);
                Position drt = new Position(row - 1, col + 1);
                if (row > 1 && col < 6 && squarePieceIdMap.get(drt) != null && squarePieceIdMap.get(drt) >= 12 && squarePieceIdMap.get(drtj) == null) {
                    moreJumpAvailable = true;
                }
                if (!moreJumpAvailable) {
                    Position dltj = new Position(row - 2, col - 2);
                    Position dlt = new Position(row - 1, col - 1);
                    if (row > 1 && col > 1 && squarePieceIdMap.get(dlt) != null && squarePieceIdMap.get(dlt) >= 12 && squarePieceIdMap.get(dltj) == null) {
                        moreJumpAvailable = true;
                    }
                }
            }
        }
        return moreJumpAvailable;
    }

    private boolean validateBackJump(Move move, int pieceId, Board board) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        Map<Position, Integer> squarePieceIdMap = board.getSquarePieceIdMap();
        //
        if (pieceId >= 12) {
            if (end.getRow() == st.getRow() + 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Position oppPos = new Position(st.getRow() + 1, st.getCell() + 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() + 1).getSquares().get(st.getCell() + 1).setPiece(null);

                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Position oppPos = new Position(st.getRow() + 1, st.getCell() - 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() + 1).getSquares().get(st.getCell() - 1).setPiece(null);
                    }
                }
            }
        } else {
            if (end.getRow() == st.getRow() - 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Position oppPos = new Position(st.getRow() - 1, st.getCell() + 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() - 1).getSquares().get(st.getCell() + 1).setPiece(null);
                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Position oppPos = new Position(st.getRow() - 1, st.getCell() - 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() - 1).getSquares().get(st.getCell() - 1).setPiece(null);
                    }
                }
            }
        }
        return isValidMove;

    }


    private void checkIfPieceMovedBecameKing(int endRow, Piece pieceMoved, Board board) {
        if (endRow == 0 || endRow == 7) {
            pieceMoved.setType(PieceType.KING);
            board.getKingPieces().add(pieceMoved.getPieceId());

        }
    }

    private boolean validateForwardJump(Move move, int pieceId, Board board) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        Map<Position, Integer> squarePieceIdMap = board.getSquarePieceIdMap();
        if (pieceId < 12) {
            if (end.getRow() == st.getRow() + 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Position oppPos = new Position(st.getRow() + 1, st.getCell() + 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() + 1).getSquares().get(st.getCell() + 1).setPiece(null);

                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Position oppPos = new Position(st.getRow() + 1, st.getCell() - 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() + 1).getSquares().get(st.getCell() - 1).setPiece(null);
                    }
                }
            }
        } else {
            if (end.getRow() == st.getRow() - 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Position oppPos = new Position(st.getRow() - 1, st.getCell() + 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() - 1).getSquares().get(st.getCell() + 1).setPiece(null);
                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Position oppPos = new Position(st.getRow() - 1, st.getCell() - 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
                        squarePieceIdMap.put(oppPos, null);
                        board.getRows().get(st.getRow() - 1).getSquares().get(st.getCell() - 1).setPiece(null);
                    }
                }
            }
        }
        return isValidMove;
    }

    private boolean validateKingMove(Move move) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        if ((end.getRow() == st.getRow() + 1 || end.getRow() == st.getRow() - 1) && (end.getCell() == st.getCell() + 1 || end.getCell() == st.getCell() - 1)) {
            isValidMove = true;
        }
        return isValidMove;
    }

    private boolean validateSingleMove(Move move, int pieceId) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        if (pieceId < 12) {
            if (end.getRow() == st.getRow() + 1 && (end.getCell() == st.getCell() + 1 || end.getCell() == st.getCell() - 1)) {
                isValidMove = true;
            }
        } else {
            if (end.getRow() == st.getRow() - 1 && (end.getCell() == st.getCell() + 1 || end.getCell() == st.getCell() - 1)) {
                isValidMove = true;
            }
        }
        return isValidMove;

    }

    public boolean amIWinning(Game game, Board board, String username) {
        boolean flag = true;
        //check if opponents pieces are null
        if (game.getPlayerOne().getUserName().equals(username)) {
            for (Position position :
                    board.getSquarePieceIdMap().keySet()) {
                if (board.getSquarePieceIdMap().get(position) != null && board.getSquarePieceIdMap().get(position) < 12) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                game.setHasGameEnded(true);
                game.setWinner(game.getPlayerOne().getUserName());
                game.setLoser(game.getPlayerTwo().getUserName());
            }
            return flag;
        } else {
            for (Position position :
                    board.getSquarePieceIdMap().keySet()) {
                if (board.getSquarePieceIdMap().get(position) != null && board.getSquarePieceIdMap().get(position) >= 12) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                game.setHasGameEnded(true);
                game.setWinner(game.getPlayerTwo().getUserName());
                game.setLoser(game.getPlayerOne().getUserName());
            }
            return flag;
        }

    }
}
