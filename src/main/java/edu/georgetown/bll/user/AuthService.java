package edu.georgetown.bll.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.georgetown.dao.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AuthService {

    private static Logger logger;
    private static Users users;
    private static HashMap<String, Cookie> cookieStore;

    public AuthService(Logger log, Users u) {
        logger = log;
        logger.info("AuthService created");
        cookieStore = new HashMap<String, Cookie>();
        users = u;
    }

    protected static boolean checkPassword(String username, String password) {
        Iterator<User> it = users.getUsers().iterator();
        while (it.hasNext()) {
            User user = it.next();
            if (user.getUsername().equals(username)) {
                return user.checkPassword(password);
            }
        }
        return false; // no such user
    }

    static public class NewLoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            InputStream requestBody = exchange.getRequestBody();
            String r = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            String username = null, password = null;

            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(r);

                username = (String) jo.get("username");
                password = (String) jo.get("password");

                logger.info("attempted login from " + username);

            } catch (Exception e) {
                logger.warning("Invalid JSON data received: " + e.getStackTrace());
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
                return;
            }

            String response;
            // if the password is correct, create a new cookie, store it, and return it
            if (checkPassword(username, password)) {
                logger.info( "successful login from " + username );
                Cookie c = new Cookie();
                cookieStore.put(username, c);
                // Send the response
                response = c.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);                
            } else {
                logger.info( "unsuccessful login from " + username );
                response = "auth failed";
                exchange.sendResponseHeaders(401, response.getBytes().length);
            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

}