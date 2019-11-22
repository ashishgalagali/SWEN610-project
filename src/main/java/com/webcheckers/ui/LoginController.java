package com.webcheckers.ui;

import com.webcheckers.dao.WebCheckersDAO;
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

    @Override
    public ModelAndView handle(Request request, Response response) {

        if (request.requestMethod() == "GET"){
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Login!");
            return new ModelAndView(vm, "login.ftl");

        } else {
            Map<String, Object> vm = new HashMap<>();
            final Session session = request.session();

            String username = request.queryParams("username");
            String password = request.queryParams("password");

            Human human = getDatastore().get(Human.class, username);

            if( human != null){
                if (human.getPassword().equals(password)){
                    session.attribute("username", username);
                    response.redirect("/game");
                    halt();
                    return null;
                } else {
                    vm.put("title","Login");
                    vm.put("messageType", "error");
                    vm.put("message", WRONG_PASSWORD);
                    return new ModelAndView(vm, "login.ftl");
                }

            } else {
                vm.put("title","Login");
                vm.put("messageType", "error");
                vm.put("message", WRONG_USERNAME);
                return new ModelAndView(vm, "login.ftl");
            }
        }

    }

}
