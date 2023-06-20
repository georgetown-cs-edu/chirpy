package edu.georgetown.chirpy.dev.src.main.java.edu.georgetown.chirpy.bll.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.georgetown.chirpy.dev.src.main.java.edu.georgetown.chirpy.dao.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class UserService {

    private static Logger logger;
    final static int PORT = 8080;
    private static Users users;

    public UserService(Logger log) {
        logger = log;
        users = new Users(logger);
        logger.info("UserService created; reading user file");
        users.ReadUsers();
        startService();
    }

    private void startService() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/newuser/", new NewUserHandler());
            server.setExecutor(null); // Use the default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Server started on port " + PORT);
    }

    static class NewUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            InputStream requestBody = exchange.getRequestBody();

            try {
                // Create a JsonReader to read the JSON data
                JsonReader reader = Json.createReader(requestBody);

                // Parse the JSON data into a JsonStructure
                JsonStructure jsonStructure = reader.read();

                JsonObject jsonObject = null;
                // If the JSON data is an object, you can cast it to a JsonObject
                if (jsonStructure instanceof JsonObject) {
                    jsonObject = (JsonObject) jsonStructure;
                } else {
                    exchange.sendResponseHeaders(500, 0);
                    return;
                }

                // Close the JsonReader
                reader.close();

                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                String name = jsonObject.getString("name");
                User user = new User(username, name, password);
                users.addUser(user);

            } catch (Exception e) {
                logger.warning("Invalid JSON data received: " + e.getStackTrace());
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
                return;
            }

            // Send the response
            String response = "Data received and processed successfully";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}