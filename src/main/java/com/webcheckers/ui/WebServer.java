package com.webcheckers.ui;

import com.webcheckers.appl.WebCheckersController;
import spark.TemplateEngine;

import static spark.Spark.*;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * guessing game application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author kirtanasuresh
 */
public class WebServer {

    //
    // Constants
    //

    /**
     * The URL pattern to request the Home page.
     */
    public static final String HOME_URL = "/";

    public static final String GAME_URL = "/game";

    public static final String LOGIN_URL = "/login";

    public static final String REGISTER_URL = "/register";

    public static final String LOGOUT_URL = "/logout";

    public static final String WAITING_URL = "/waiting";

    //
    // Attributes
    //

    private final TemplateEngine templateEngine;

    private final WebCheckersController webCheckersController;

    //
    // Constructor
    //

    /**
     * The constructor for the Web Server.
     *
     * @param templateEngine The default {@link TemplateEngine} to render views.
     */
    public WebServer(
            final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.webCheckersController = new WebCheckersController();
    }

    //
    // Public methods
    //

    /**
     * Initialize all of the HTTP routes that make up this web application.
     *
     * <p>
     * Initialization of the web server includes defining the location for static
     * files, and defining all routes for processing client requests. The method
     * returns after the web server finishes its initialization.
     * </p>
     */
    public void initialize() {
        // Configuration to serve static files
        staticFileLocation("/public");

        //// Setting any route (or filter) in Spark triggers initialization of the
        //// embedded Jetty web server.

        //// A route is set for a request verb by specifying the path for the
        //// request, and the function callback (request, response) -> {} to
        //// process the request. The order that the routes are defined is
        //// important. The first route (request-path combination) that matches
        //// is the one which is invoked. Additional documentation is at
        //// http://sparkjava.com/documentation.html and in Spark tutorials.

        //// Each route (processing function) will check if the request is valid
        //// from the client that made the request. If it is valid, the route
        //// will extract the relevant data from the request and pass it to the
        //// application object delegated with executing the request. When the
        //// delegate completes execution of the request, the route will create
        //// the parameter map that the response template needs. The data will
        //// either be in the value the delegate returns to the route after
        //// executing the request, or the route will query other application
        //// objects for the data needed.

        //// FreeMarker defines the HTML response using templates. Additional
        //// documentation is at
        //// http://freemarker.org/docs/dgui_quickstart_template.html.
        //// The Spark FreeMarkerEngine lets you pass variable values to the
        //// template via a map. Additional information is in online
        //// tutorials such as
        //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

        //// These route definitions are examples. You will define the routes
        //// that are appropriate for the HTTP client interface that you define.
        //// Create separate Route classes to handle each route; this keeps your
        //// code clean; using small classes.

        // Shows the Checkers game Home page.
        get(HOME_URL, new HomeController(), templateEngine);
        get(GAME_URL, new GameController(), templateEngine);

        //login get post
        get(LOGIN_URL, new LoginController(webCheckersController), templateEngine);
        post(LOGIN_URL, new LoginController(webCheckersController), templateEngine);

        //register get post
        get(REGISTER_URL, new RegisterController(), templateEngine);
        post(REGISTER_URL, new RegisterController(), templateEngine);

        get(LOGOUT_URL, new LogoutController(), templateEngine);

        get(WAITING_URL, new WaitingController(webCheckersController), templateEngine);

    }

}