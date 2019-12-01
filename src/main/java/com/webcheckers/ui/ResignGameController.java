package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.*;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class ResignGameController implements Route {
    @Override
    public ModelAndView handle(Request request, Response response) {
        if (request.session().attribute("username") == null) {
            response.redirect("/login");
            halt();
            return null;
        }
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        game.setHasGameEnded(true);
        game.setLoser(userName);
        if (game.getPlayerOne().equals(userName)) game.setWinner(game.getPlayerTwo().getUserName());
        else game.setWinner(game.getPlayerOne().getUserName());

        Map<String, Object> vm = setPlayerVariables(game, userName);
        vm.put("ended", true);
        if (game.getPlayerOne().getUserName().equals(userName)) {
            if (game.getWinner().equals(userName)) {
                Message message = new Message("Congratulations! You Won!", MessageType.info);
                vm.put("message", message);
                vm.put("points", game.getPlayerOne().getPoints() + 1);
            } else {
                Message message = new Message("Sorry, you lost, better luck next time", MessageType.error);
                vm.put("message", message);
                vm.put("points", game.getPlayerOne().getPoints());
            }
        } else {
            if (game.getWinner().equals(userName)) {
                Message message = new Message("Congratulations! You Won!", MessageType.info);
                vm.put("message", message);
                vm.put("points", game.getPlayerTwo().getPoints() + 1);
            } else {
                Message message = new Message("Sorry, you lost, better luck next time", MessageType.error);
                vm.put("message", message);
                vm.put("points", game.getPlayerTwo().getPoints());
            }
        }
        return new ModelAndView(vm, "game.ftl");
    }

    public Map<String, Object> setPlayerVariables(Game game, String userName) {
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
        return vm;
    }
}
