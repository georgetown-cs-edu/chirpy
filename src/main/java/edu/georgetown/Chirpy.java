/**
 * Chirpy -- a really basic social networking site
 * 
 * Micah Sherr <msherr@cs.georgetown.edu>
 */

package edu.georgetown;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.javalin.Javalin;
import edu.georgetown.bll.user.UserService;
import edu.georgetown.dl.DefaultPageHandler;
import edu.georgetown.dl.DisplayLogic;
import edu.georgetown.dl.ListCookiesHandler;
import edu.georgetown.dl.TestFormHandler;

public class Chirpy {

    final static int PORT = 8080;

    private Logger logger;
    private DisplayLogic displayLogic;

    public Chirpy() {
        /* 
         * A Logger is a thing that records "log" messages.  This is better
         * than using System.out.println() because you can control which
         * messages are logged.  For example, you can log only "warning"
         * messages, or you can log all messages.
         * 
         * We'll create one logger, call it `logger`, and then pass this
         * logger to our classes.  This way, all of our classes will log
         * to the same place.
         */
        logger = Logger.getLogger("MyLogger");
        Formatter logFormatter = new Formatter() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public String format(java.util.logging.LogRecord record) {
                String timestamp = Instant.ofEpochMilli(record.getMillis())
                        .atZone(ZoneId.systemDefault())
                        .format(dateFormatter);
                return String.format("%s %s %s%n", timestamp, record.getLevel().getName(), formatMessage(record));
            }
        };

        try {
            FileHandler fileHandler = new FileHandler("/tmp/log.txt");
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(logFormatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        consoleHandler.setFormatter(logFormatter);
        logger.setUseParentHandlers(false); // Remove default handlers
        // Set desired log level (e.g., Level.INFO, Level.WARNING, etc.)
        logger.setLevel(Level.ALL); 

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
        Javalin app = Javalin.create().start(PORT);

        DefaultPageHandler defaultPageHandler = new DefaultPageHandler(logger, displayLogic);
        TestFormHandler testFormHandler = new TestFormHandler(logger, displayLogic);
        ListCookiesHandler listCookiesHandler = new ListCookiesHandler(logger, displayLogic);

        // each of these routes below indicates a URL path that will be handled by
        // the service.
        app.get("/formtest/", ctx -> testFormHandler.handle(ctx));
        app.post("/formtest/", ctx -> testFormHandler.handle(ctx));
        app.get("/listcookies/", ctx -> listCookiesHandler.handle(ctx));
        app.get("/", ctx -> defaultPageHandler.handle(ctx));
        // you will need to add to the above list to add new functionality to the web
        // service.

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
