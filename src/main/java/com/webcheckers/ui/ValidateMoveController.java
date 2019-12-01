package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public class ValidateMoveController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

        Message message = null;
        boolean isValidMove = false;

        if (request.body().toString().contains("{}")) {
            //TODO!!!
            message = new Message("You have moved to the same position!!!Refresh page", MessageType.error);
            return new Gson().toJson(message, Message.class);
        }
        Move move = new Gson().fromJson(request.body(), Move.class);
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
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

        //if opponent set is empty i win
        if (!isValidMove) {
//            if (pieceMoved.getType().equals(PieceType.SINGLE)) {
            isValidMove = validateForwardJump(move, pieceMoved.getPieceId(), board);
            if (!isValidMove && pieceMoved.getType().equals(PieceType.KING)) {
                isValidMove = validateBackJump(move, pieceMoved.getPieceId(), board);
            }
//            } else {
//                isValidMove = validateKingJump(move, pieceMoved.getPieceId(), board);
//            }
        }

        if (isValidMove) {
            checkIfPieceMovedBecameKing(move.getEnd().getRow(), pieceMoved);
            board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).setPiece(null);
            board.getRows().get(move.getEnd().getRow()).getSquares().get(move.getEnd().getCell()).setPiece(pieceMoved);
            board.getSquarePieceIdMap().remove(move.getStart());
            board.getSquarePieceIdMap().put(move.getEnd(), pieceMoved.getPieceId());
            //check if all opponent peices are null
            System.out.println(userName + "++++" + amIWinning(game, board, userName));
            message = new Message("Valid move. Hit Submit move!!!", MessageType.info);
        } else {
            message = new Message("Invalid Move!!!", MessageType.error);
        }
        return new Gson().toJson(message, Message.class);
    }

    private boolean validateBackJump(Move move, int pieceId, Board board) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        Map<Position, Integer> squarePieceIdMap = board.getSquarePieceIdMap();
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


    private void checkIfPieceMovedBecameKing(int endRow, Piece pieceMoved) {
        if (endRow == 0 || endRow == 7) {
            pieceMoved.setType(PieceType.KING);
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
        System.out.println("IsvalidMove: " + isValidMove);
        return isValidMove;

    }

    public boolean amIWinning(Game game, Board board, String username){
        boolean flag = true;
        //check if opponents pieces are null
        if(game.getPlayerOne().getUserName().equals(username)) {
            for (Position position:
                 board.getSquarePieceIdMap().keySet()) {
                if(board.getSquarePieceIdMap().get(position) != null && board.getSquarePieceIdMap().get(position) < 12){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                game.setHasGameEnded(true);
                game.setWinner(game.getPlayerOne().getUserName());
                game.setLoser(game.getPlayerTwo().getUserName());
            }
            return flag;
        } else {
            for (Position position:
                    board.getSquarePieceIdMap().keySet()) {
                if(board.getSquarePieceIdMap().get(position) != null && board.getSquarePieceIdMap().get(position) >= 12){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                game.setHasGameEnded(true);
                game.setWinner(game.getPlayerTwo().getUserName());
                game.setLoser(game.getPlayerOne().getUserName());
            }
            return flag;
        }

    }
}
