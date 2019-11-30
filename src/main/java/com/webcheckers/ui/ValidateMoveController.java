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

        if (!isValidMove) {
            if (pieceMoved.getType().equals(PieceType.SINGLE)) {
                isValidMove = validateSingleJump(move, pieceMoved.getPieceId(), board.getSquarePieceIdMap());
            } else {
                isValidMove = validateKingMove(move);
            }
        }

        if (isValidMove) {
            board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).setPiece(null);
            board.getRows().get(move.getEnd().getRow()).getSquares().get(move.getEnd().getCell()).setPiece(pieceMoved);
            board.getSquarePieceIdMap().remove(move.getStart());
            board.getSquarePieceIdMap().put(move.getEnd(), pieceMoved.getPieceId());
            message = new Message("Valid move. Hit Submit move!!!", MessageType.info);
        } else {
            message = new Message("Invalid Move!!!", MessageType.error);
        }
        return new Gson().toJson(message, Message.class);
    }

    private boolean validateSingleJump(Move move, int pieceId, Map<Position, Integer> squarePieceIdMap) {
        boolean isValidMove = false;
        Position st = move.getStart();
        Position end = move.getEnd();
        if (pieceId < 12) {
            if (end.getRow() == st.getRow() + 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Position oppPos = new Position(st.getRow() + 1, st.getCell() + 1);
                    Integer oppPieceId = squarePieceIdMap.get(oppPos);
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;


                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Integer oppPieceId = squarePieceIdMap.get(new Position(st.getRow() + 1, st.getCell() - 1));
                    if (oppPieceId != null && oppPieceId >= 12) {
                        isValidMove = true;
                    }
                }
            }
        } else {
            if (end.getRow() == st.getRow() - 2) {
                if (end.getCell() == st.getCell() + 2) {
                    Integer oppPieceId = squarePieceIdMap.get(new Position(st.getRow() - 1, st.getCell() + 1));
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
                    }
                } else if (end.getCell() == st.getCell() - 2) {
                    Integer oppPieceId = squarePieceIdMap.get(new Position(st.getRow() - 1, st.getCell() - 1));
                    if (oppPieceId != null && oppPieceId < 12) {
                        isValidMove = true;
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
}
