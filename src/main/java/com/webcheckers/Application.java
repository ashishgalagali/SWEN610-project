package com.webcheckers;

import com.webcheckers.appl.WebCheckersController;
import com.webcheckers.model.Color;
import com.webcheckers.model.Human;
import com.webcheckers.ui.WebServer;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.logging.Logger;


/**
 * The entry point for the WebCheckers web application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    //
    // Application Launch method
    //

    /**
     * Entry point for the WebCheckers Character.isDigit(split2[1].charAt(0))web application.
     *
     * <p>
     * It wires the application components together.  This is an example
     * of <a href='https://en.wikipedia.org/wiki/Dependency_injection'>Dependency Injection</a>
     * </p>
     *
     * @param args Command line arguments; none expected.
     */
    public static void main(String[] args) {

        // The application uses FreeMarker templates to generate the HTML
        // responses sent back to the client. This will be the engine processing
        // the templates and associated data.
        final TemplateEngine templateEngine = new FreeMarkerEngine();

        // inject the game center and freemarker engine into web server
        final WebServer webServer = new WebServer(templateEngine);

        // inject web server into application
        final Application app = new Application(webServer);

        final WebCheckersController webCheckersController = new WebCheckersController();

        // start the application up
        app.initialize();
    }

    //
    // Attributes
    //

    private final WebServer webServer;

    //
    // Constructor
    //

    private Application(final WebServer webServer) {
        this.webServer = webServer;
    }

    //
    // Private methods
    //

    private void initialize() {
        LOG.fine("WebCheckers is initializing.");

        //Testing lombok
        Human player = new Human();
        player.setPlayerId("testPID");
        player.setPlayerColor(Color.WHITE);
        player.setEmail("as@qw.in");
        System.out.println(player.getPlayerId());
        System.out.println(player.getPlayerColor());
        System.out.println(player.getEmail());

        // configure Spark and startup the Jetty web server
        webServer.initialize();

        // other applications might have additional services to configure

        LOG.info("WebCheckers initialization complete.");
    }

}