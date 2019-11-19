package com.webcheckers.ui;

import com.webcheckers.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();
        Player player1 = new Player();
        player1.setPlayerId("1");
        player1.setPlayerColor(Color.BLACK);
        player1.setPlayerName("AJ");
        Player player2 = new Player();
        player2.setPlayerId("2");
        player2.setPlayerColor(Color.WHITE);
        player2.setPlayerName("JG");
        vm.put("playerName", "Ashish");
        vm.put("playerColor", Color.BLACK);
        vm.put("isMyTurn", true);
        vm.put("title", "TITLE");
        vm.put("opponentName", "John");
        vm.put("opponentColor", Color.WHITE);

        vm.put("currentPlayer", player1);

        Message message = new Message("Just setting up!!!", MessageType.info);
        vm.put("message", message);

        List<Row> rows = new ArrayList<>();

        List<Space> oddSpaces = new ArrayList<>();
        oddSpaces.add(new Space(0, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        oddSpaces.add(new Space(1, false, null));
        oddSpaces.add(new Space(2, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        oddSpaces.add(new Space(3, false, null));
        oddSpaces.add(new Space(4, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        oddSpaces.add(new Space(5, false, null));
        oddSpaces.add(new Space(6, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        oddSpaces.add(new Space(7, false, null));

        List<Space> evenSpaces = new ArrayList<>();
        evenSpaces.add(new Space(0, false, null));
        evenSpaces.add(new Space(1, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        evenSpaces.add(new Space(2, false, null));
        evenSpaces.add(new Space(3, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        evenSpaces.add(new Space(4, false, null));
        evenSpaces.add(new Space(5, true, new Piece(PieceType.SINGLE, Color.WHITE)));
        evenSpaces.add(new Space(6, false, null));
        evenSpaces.add(new Space(7, true, new Piece(PieceType.SINGLE, Color.WHITE)));


        rows.add(new Row(0, oddSpaces));
        rows.add(new Row(1, evenSpaces));
        rows.add(new Row(2, oddSpaces));
        rows.add(new Row(3, evenSpaces));
        rows.add(new Row(4, oddSpaces));
        rows.add(new Row(5, evenSpaces));
        rows.add(new Row(6, oddSpaces));
        rows.add(new Row(7, evenSpaces));

        Board board = new Board(rows);

        vm.put("board", board);

        return new ModelAndView(vm, "game.ftl");
    }

}
