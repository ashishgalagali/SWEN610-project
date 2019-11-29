package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
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
        String userName = request.session().attribute("username");
        System.out.println("UID: " + userName);
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);

        Map<String, Object> vm = new HashMap<>();

        vm.put("title", game.getPlayerOne().getUserName() + " vs " + game.getPlayerTwo().getUserName());
        if (game.getPlayerOne().getUserName().equals(userName)) {
            Player player1 = new Player();
            player1.setPlayerId(game.getPlayerOne().getPlayerId());
            player1.setPlayerColor(Color.RED);
            player1.setPlayerName(game.getPlayerOne().getPlayerName());
            vm.put("isMyTurn", game.isPlayerOneTurn());
            vm.put("playerName", game.getPlayerOne().getUserName());
            vm.put("playerColor", Color.RED);
            vm.put("opponentName", game.getPlayerTwo().getUserName());
            vm.put("opponentColor", Color.WHITE);
            vm.put("currentPlayer", player1);
        } else {
            Player player2 = new Player();
            player2.setPlayerId(game.getPlayerTwo().getPlayerId());
            player2.setPlayerColor(Color.WHITE);
            player2.setPlayerName(game.getPlayerTwo().getPlayerName());
            vm.put("isMyTurn", !game.isPlayerOneTurn());
            vm.put("playerName", game.getPlayerTwo().getUserName());
            vm.put("playerColor", Color.WHITE);
            vm.put("opponentName", game.getPlayerOne().getUserName());
            vm.put("opponentColor", Color.RED);
            vm.put("currentPlayer", player2);
        }

        Message message = new Message("All the best!!!", MessageType.info);
        vm.put("message", message);

        if (game.getBoard() == null) {
            game.setBoard(new Board());
        }
        System.out.println(vm);

        vm.put("board", game.getBoard());

        return new ModelAndView(vm, "game.ftl");
    }

}
