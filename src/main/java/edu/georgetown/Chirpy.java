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
import edu.georgetown.bll.user.AuthService;
import edu.georgetown.bll.user.UserService;
import edu.georgetown.dl.DefaultPageHandler;
import edu.georgetown.dl.DisplayLogic;
import edu.georgetown.dl.TestFormHandler;

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
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/formtest/", new TestFormHandler(logger, displayLogic));
            server.createContext("/newuser/", new UserService.NewUserHandler());
            server.createContext("/listusers/", new UserService.ListUserHandler());
            server.createContext("/login/", new AuthService.NewLoginHandler());
            server.createContext("/", new DefaultPageHandler(logger, displayLogic));
            server.setExecutor(null); // Use the default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Server started on port " + PORT);
    }

    public static void main(String[] args) {

        Chirpy ws = new Chirpy();
        UserService userService = new UserService(ws.logger);
        new AuthService(ws.logger, userService.getUsers());
        ws.startService();
    }

    


}
