package edu.georgetown.dl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TestFormHandler implements HttpHandler {

    final String FORM_PAGE = "formtest.thtml";
    private Logger logger;
    private DisplayLogic displayLogic;

    public TestFormHandler(Logger log, DisplayLogic dl) {
        logger = log;
        displayLogic = dl;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("TestFormHandler called");

        // dataModel will hold the data to be used in the template
        Map<String, Object> dataModel = new HashMap<String, Object>();

        byte[] b = exchange.getRequestBody().readAllBytes();
        String formData = new String(b);

        logger.info("got " + formData);
        Map<String, String> myMap = null;
        if (!formData.equals("")) {
            myMap = DisplayLogic.parseResponse(formData);
            if (myMap.containsKey("username")) {
                dataModel.put("username", myMap.get("username"));
            }
        } 

        // sw will hold the output of parsing the template
        StringWriter sw = new StringWriter();

        // now we call the display method to parse the template and write the output
        displayLogic.parseTemplate(FORM_PAGE, dataModel, sw);

        // set the type of content (in this case, we're sending back HTML)
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        // send the HTTP headers
        exchange.sendResponseHeaders(200, (sw.getBuffer().length()));
        // finally, write the actual response (the contents of the template)
        OutputStream os = exchange.getResponseBody();
        os.write(sw.toString().getBytes());
        os.close();
    }
}
