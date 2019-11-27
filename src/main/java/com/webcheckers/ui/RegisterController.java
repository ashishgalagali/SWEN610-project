package com.webcheckers.ui;

import com.webcheckers.dao.WebCheckersDAO;
import com.webcheckers.model.Human;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static spark.Spark.halt;

/**
 * @author kirtanasuresh
 */
public class RegisterController extends WebCheckersDAO implements TemplateViewRoute {
    private static final String VALIDATE_EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Override
    public ModelAndView handle(Request request, Response response) {
        if (request.requestMethod() == "GET") {
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Register!");
            return new ModelAndView(vm, "register.ftl");
        } else {
            //Prepare the VM & get username, type, & logged in status
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Register");

            //Pull in form data
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String email = request.queryParams("email");
            //removing roleid, will be set as 1 for default as only authors can register
            String role = "1";

            if (validatePassword(password)) {
                if (validateEmail(email)) {
                    if (!validateUsername(username)) {
                        System.out.println(getDatastore().save(new Human(username, email, password)));
                        vm.put("smessage", "Successfully registered, please login!");
                        vm.put("messageType", "success");
                        return new ModelAndView(vm, "login.ftl");
//                        response.redirect("/login");
//                        halt();
//                        return null;
                    } else {
                        vm.put("message", "username already exists");
                        vm.put("messageType", "error");
                        return new ModelAndView(vm, "register.ftl");
                    }
                } else {
                    vm.put("message", "Invalid email");
                    vm.put("messageType", "error");
                    return new ModelAndView(vm, "register.ftl");
                }
            } else {
                vm.put("message", "Password must be at least 4 characters");
                vm.put("messageType", "error");
                return new ModelAndView(vm, "register.ftl");
            }
        }
    }

    /**
     * Validate that password must have digits and must be at least 4 characters
     *
     * @param password password to verify
     * @return if password is valid
     */
    public boolean validatePassword(String password) {
        if (password == null) return false;
        return password.length() >= 4;
    }

    /**
     * Email must be in correct format
     *
     * @param email email to validate
     * @return if email is valid
     */
    public boolean validateEmail(String email) {
        if (email == null) return false;
        Pattern pattern = Pattern.compile(VALIDATE_EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }


    /**
     * Check if username exists
     * @param username username provided
     * @return true if exists in DB
     */
    public boolean validateUsername(String username) {
        if (username == null) return false;
        Human u = getDatastore().get(Human.class, username);
        if (u != null) return true;
        return false;
    }
}
