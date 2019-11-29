package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class ValidateMoveController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

        Message message = null;

        Move move = new Gson().fromJson(request.body(), Move.class);
        message = new Message("Validating move", MessageType.info);
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        Board board = game.getBoard();
        Piece pieceMoved = board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).getPiece();

        board.getRows().get(move.getStart().getRow()).getSquares().get(move.getStart().getCell()).setPiece(null);
        board.getRows().get(move.getEnd().getRow()).getSquares().get(move.getEnd().getCell()).setPiece(pieceMoved);
        return new Gson().toJson(message, Message.class);
    }
}
