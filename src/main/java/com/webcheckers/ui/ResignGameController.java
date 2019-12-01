package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.Game;
import spark.*;

public class ResignGameController implements Route {
    @Override
    public Object handle(Request request, Response response) {
        String userName = request.session().attribute("username");
        Game game = WebCheckersController.getInstance().getUserGame().get(userName);
        //if game has already ended
        if(game.isHasGameEnded()) response.redirect("/game");
        game.setHasGameEnded(true);
        game.setLoser(userName);
        if(game.getPlayerOne().getUserName().equals(userName)) game.setWinner(game.getPlayerTwo().getUserName());
        else game.setWinner(game.getPlayerOne().getUserName());
        response.redirect("/game");
        return true;
    }
}
