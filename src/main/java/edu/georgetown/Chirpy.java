/**
 * Chirpy -- a really basic social networking site
 * 
 * Micah Sherr <msherr@cs.georgetown.edu>
 */

package edu.georgetown;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.georgetown.bll.user.AuthService;
import edu.georgetown.bll.user.UserService;
import edu.georgetown.dl.DisplayLogic;

public class Chirpy {

    final static int PORT = 8080;
    final String DEFAULT_PAGE = "micahtest.thtml";

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

    public static void main(String[] args) {

        Chirpy ws = new Chirpy();
        UserService userService = new UserService(ws.logger);
        new AuthService(ws.logger, userService.getUsers());
        ws.startService();
    }

    public class DefaultPageHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.info("DefaultPageHandler called");

            // sw will hold the output of parsing the template
            StringWriter sw = new StringWriter();
            // dataModel will hold the data to be used in the template
            Map<String, Object> dataModel = new HashMap<String, Object>();

            {
                // I'm putting this in a code block because it's really just demo
                // code.  We're populating the dataModel with some example data
                // that's not particularly useful

                // the "date" variable in the template will be set to the current date
                dataModel.put("date", new Date().toString());
                // and randvector will be a vector of random doubles (just for illustration)
                Vector<Double> v = new Vector<Double>();
                for (int i = 0; i < 10; i++) {
                    v.add(Math.random());
                }
                dataModel.put("randvector", v);
                
            }

            displayLogic.display(DEFAULT_PAGE, dataModel, sw);
            
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, (sw.getBuffer().length())); 
            OutputStream os = exchange.getResponseBody();
            os.write(sw.toString().getBytes());
            os.close();
        }
    }

    private void startService() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/newuser/", new UserService.NewUserHandler());
            server.createContext("/listusers/", new UserService.ListUserHandler());
            server.createContext("/login/", new AuthService.NewLoginHandler());
            server.createContext("/", new DefaultPageHandler());
            server.setExecutor(null); // Use the default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Server started on port " + PORT);
    }

}
