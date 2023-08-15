package edu.georgetown.bll.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.georgetown.dao.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UserService {

    private static Logger logger;
    private static Users users;

    public UserService(Logger log) {
        logger = log;
        users = new Users(logger);
        logger.info("UserService created; reading user file");
        users.readUsers();
    }

    public Users getUsers() {
        return users;
    }

    static public class NewUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            InputStream requestBody = exchange.getRequestBody();
            String r = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(r);

                String username = (String) jo.get("username");
                String password = (String) jo.get("password");
                String name = (String) jo.get("name");
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

    static public class ListUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Vector<User> userList = users.getUsers();

            JSONArray arr = new JSONArray();

            Map<String, String> m = new LinkedHashMap<String, String>(userList.size());

            Iterator<User> it = userList.iterator();
            while (it.hasNext()) {
                User user = it.next();
                m.put("username", user.getUsername());
                m.put("name", user.getName());
                arr.add(m);
            }

            // Send the response
            String response = arr.toJSONString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }
}