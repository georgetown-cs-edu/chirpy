package edu.georgetown.chirpy;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import edu.georgetown.chirpy.bll.user.UserService;

class WebService {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("MyLogger");
        try {
            FileHandler fileHandler = new FileHandler("/tmp/log.txt");
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        //logger.setUseParentHandlers(false); // Remove default handlers
        logger.setLevel(Level.ALL); // Set desired log level (e.g., Level.INFO, Level.WARNING, etc.)

        logger.info("Starting chirpy web service");
        
        UserService UserService = new UserService(logger);


    }

}
