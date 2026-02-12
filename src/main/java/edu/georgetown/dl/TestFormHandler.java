package edu.georgetown.dl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import io.javalin.http.Context;

public class TestFormHandler {

    final String FORM_PAGE = "formtest.thtml";
    private Logger logger;
    private DisplayLogic displayLogic;

    public TestFormHandler(Logger log, DisplayLogic dl) {
        logger = log;
        displayLogic = dl;
    }

    public void handle(Context ctx) {
        logger.info("TestFormHandler called");

        // dataModel will hold the data to be used in the template
        Map<String, Object> dataModel = new HashMap<String, Object>();

        // use Javalin's built-in form parameter parsing
        String username = ctx.formParam("username");

        // if the web form contained a username, add it to the data model
        if (username != null) {
            dataModel.put("username", username);
        }

        // sw will hold the output of parsing the template
        StringWriter sw = new StringWriter();

        // now we call the display method to parse the template and write the output
        displayLogic.parseTemplate(FORM_PAGE, dataModel, sw);

        // if we have a username, set a cookie with the username.
        if (username != null) {
            displayLogic.addCookie(ctx, "username", username);
        }

        // set the type of content (in this case, we're sending back HTML)
        ctx.contentType("text/html");
        ctx.result(sw.toString());
    }
}
