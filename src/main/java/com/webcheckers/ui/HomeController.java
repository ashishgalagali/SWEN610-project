package com.webcheckers.ui;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * The Web Controller for the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class HomeController implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();
        if (request.session().attribute("username") != null) {
            vm.put("title", "Welcome to Checkers Game!");
            return new ModelAndView(vm, "home.ftl");
        } else { //else redirect to the login page
            response.redirect("/login");
            halt();
            return null;
        }
    }
}