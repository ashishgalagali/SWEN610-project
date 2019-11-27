package com.webcheckers.ui;

import com.webcheckers.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

public class GameController implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();
        Player player1 = new Player();
        player1.setPlayerId("1");
        player1.setPlayerColor(Color.RED);
        player1.setPlayerName("AJ");
        Player player2 = new Player();
        player2.setPlayerId("2");
        player2.setPlayerColor(Color.WHITE);
        player2.setPlayerName("JG");
        vm.put("playerName", "Ashish");
        vm.put("playerColor", Color.RED);
        vm.put("isMyTurn", true);
        vm.put("title", "TITLE");
        vm.put("opponentName", "John");
        vm.put("opponentColor", Color.WHITE);

        vm.put("currentPlayer", player1);

        Message message = new Message("Just setting up!!!", MessageType.info);
        vm.put("message", message);

        Board board = new Board();

        vm.put("board", board);

        return new ModelAndView(vm, "game.ftl");
    }

}
