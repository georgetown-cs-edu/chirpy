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

public class WebService {

    final static int PORT = 8080;
    private Logger logger;

    public WebService() {
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

        logger.info("Starting chirpy web service");

    }

    public static void main(String[] args) {

        WebService ws = new WebService();
        UserService userService = new UserService(ws.logger);
        ws.startService(userService);

    }

    private void startService(UserService userService) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/newuser/", new UserService.NewUserHandler());
            server.createContext("/listusers/", new UserService.ListUserHandler());
            server.setExecutor(null); // Use the default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Server started on port " + PORT);
    }

}
