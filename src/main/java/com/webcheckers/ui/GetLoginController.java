package com.webcheckers.ui;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


import java.util.HashMap;
import java.util.Map;

/**
 * @author kirtanasuresh
 */
public class GetLoginController implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Login!");
        return new ModelAndView(vm, "login.ftl");
    }

}
