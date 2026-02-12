package edu.georgetown.dl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import io.javalin.http.Context;

public class ListCookiesHandler {

    final String COOKIELIST_PAGE = "showcookies.thtml";
    private Logger logger;
    private DisplayLogic displayLogic;

    public ListCookiesHandler(Logger log, DisplayLogic dl) {
        logger = log;
        displayLogic = dl;
    }

    public void handle(Context ctx) {
        logger.info("ListCookiesHandler called");

        // grab all of the cookies that have been set
        Map<String, String> cookies = displayLogic.getCookies(ctx);

        // dataModel will hold the data to be used in the template
        Map<String, Object> dataModel = new HashMap<String, Object>();

        dataModel.put("cookienames", cookies.keySet());
        dataModel.put("cookievalues", cookies.values());

        // sw will hold the output of parsing the template
        StringWriter sw = new StringWriter();

        // now we call the display method to parse the template and write the output
        displayLogic.parseTemplate(COOKIELIST_PAGE, dataModel, sw);

        // set the type of content (in this case, we're sending back HTML)
        ctx.contentType("text/html");
        ctx.result(sw.toString());
    }
}
