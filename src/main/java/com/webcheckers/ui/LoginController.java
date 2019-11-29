package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.dao.WebCheckersDAO;
import com.webcheckers.helper.Tuple;
import com.webcheckers.model.Game;
import com.webcheckers.model.Human;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * @author kirtanasuresh
 */
public class LoginController extends WebCheckersDAO implements TemplateViewRoute {

    String WRONG_PASSWORD = "Incorrect Password";
    String WRONG_USERNAME = "Incorrect Username";
    WebCheckersController webCheckersController = WebCheckersController.getInstance();


    @Override
    public ModelAndView handle(Request request, Response response) {

        if (request.requestMethod() == "GET") {
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Login!");
            return new ModelAndView(vm, "login.ftl");

        } else {
            Map<String, Object> vm = new HashMap<>();
            final Session session = request.session();

            String username = request.queryParams("username");
            String password = request.queryParams("password");

            Human human = getDatastore().get(Human.class, username);

            if (human != null) {
                if (human.getPassword().equals(password)) {
                    session.attribute("username", username);
                    Tuple t = webCheckersController.getOpponent(username);
                    if (t != null) {
                        webCheckersController.addPlayerToGame(human, t.game.getGameID());
                        System.out.println();
                        response.redirect("/game");
                        halt();
                        return null;
                    } else {
                        Game newGame = new Game();
                        newGame.setPlayerOne(human);
                        newGame.setGameID(webCheckersController.numberOfGames);
                        Tuple newT = new Tuple(human, newGame);
                        webCheckersController.addPlayerAndGame(newT);
                        vm.put("title", "Waiting");
                        response.redirect("/waiting");
                        return new ModelAndView(vm, "waiting.ftl");
                    }
                } else {
                    vm.put("title", "Login");
                    vm.put("messageType", "error");
                    vm.put("message", WRONG_PASSWORD);
                    return new ModelAndView(vm, "login.ftl");
                }

            } else {
                vm.put("title", "Login");
                vm.put("messageType", "error");
                vm.put("message", WRONG_USERNAME);
                return new ModelAndView(vm, "login.ftl");
            }
        }

    }

}
