/**
 * Chirpy -- a really basic social networking site
 * 
 * Micah Sherr <msherr@cs.georgetown.edu>
 */

package edu.georgetown;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.sun.net.httpserver.HttpServer;
import edu.georgetown.bll.user.UserService;
import edu.georgetown.dl.DefaultPageHandler;
import edu.georgetown.dl.DisplayLogic;
import edu.georgetown.dl.ListCookiesHandler;

public class Chirpy {

    final static int PORT = 8080;

    private Logger logger;
    private DisplayLogic displayLogic;

    public Chirpy() {
        logger = Logger.getLogger("MyLogger");
        try {
            FileHandler fileHandler = new FileHandler("/tmp/log.txt");
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false); // Remove default handlers
        logger.setLevel(Level.ALL); // Set desired log level (e.g., Level.INFO, Level.WARNING, etc.)

        try {
            displayLogic = new DisplayLogic(logger);
        } catch (IOException e) {
            logger.warning("failed to initialize display logic: " + e.getMessage());
            System.exit(1);
        }

        logger.info("Starting chirpy web service");

    }

    /**
     * Start the web service
     */
    private void startService() {
        try {
            // initialize the web server
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

            // each of these "contexts" below indicates a URL path that will be handled by
            // the service. The top-level path is "/", and that should be listed last.
            server.createContext("/formtest/", new ListCookiesHandler(logger, displayLogic));
            server.createContext("/listcookies/", new ListCookiesHandler(logger, displayLogic));
            server.createContext("/", new DefaultPageHandler(logger, displayLogic));
            // you will need to add to the above list to add new functionality to the web
            // service.  Just make sure that the handler for "/" is listed last.

            server.setExecutor(null); // Use the default executor

            // this next line effectively starts the web service and waits for requests. The
            // above "contexts" (created via `server.createContext`) will be used to handle
            // the requests.
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Server started on port " + PORT);
    }

    public static void main(String[] args) {

        Chirpy ws = new Chirpy();
        // let's start up the various business logic services
        UserService userService = new UserService(ws.logger);

        // finally, let's begin the web service so that we can start handling requests
        ws.startService();
    }

}
