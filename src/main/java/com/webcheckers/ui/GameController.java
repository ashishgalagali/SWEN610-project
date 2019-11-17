package com.webcheckers.ui;

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
        vm.put("playerName", "Ashish");
        vm.put("playerColor", "White");
        vm.put("isMyTurn", true);
        vm.put("title", "TITLE");
        vm.put("opponentName", "Vibz");
        vm.put("opponentColor", "Red");
        return new ModelAndView(vm, "game.ftl");
    }

}
