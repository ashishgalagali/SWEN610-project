package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.MessageType;
import spark.Request;
import spark.Response;
import spark.Route;

public class SubmitTurnController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String userName = request.session().attribute("username");
        System.out.println(request.session().attributes());
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        game.setPlayerOneTurn(!game.isPlayerOneTurn());
        response.redirect("/game");
        return true;
    }
}
