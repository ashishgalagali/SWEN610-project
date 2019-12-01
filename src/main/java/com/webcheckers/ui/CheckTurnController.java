package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class CheckTurnController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        System.out.println(userName);
        if(game.isHasGameEnded()){
            return true;
        }
        if (game.isPlayerOneTurn() && game.getPlayerOne().getUserName().equals(userName)) {
            return true;
        }
        if (!game.isPlayerOneTurn() && game.getPlayerTwo().getUserName().equals(userName)) {
            return true;
        }
        return false;
    }
}
