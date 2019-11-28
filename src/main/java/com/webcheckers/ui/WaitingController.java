package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * @author kirtanasuresh
 */
public class WaitingController implements TemplateViewRoute {
    private WebCheckersController webCheckersController;

    public WaitingController(WebCheckersController webCheckersController){
        this.webCheckersController = webCheckersController;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        Session session = request.session();
        String username = session.attribute("username");
        //if the player has been assigned a game, move him to game play
        if(webCheckersController.playerHasGame(username)){
            response.redirect("/game");
            halt();
            return null;
        } else {
            //else keep him in wait state
            Map<String, Object> vm = new HashMap<>();
            vm.put("title","Waiting");
            return new ModelAndView(vm, "waiting.ftl");
        }
    }

}
