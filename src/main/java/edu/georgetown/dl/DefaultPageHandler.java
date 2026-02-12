/**
 * This file contains the handler for the default (top-level) page.  
 */

package edu.georgetown.dl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;
import io.javalin.http.Context;

// to handle a web request, the class provides a `handle` method
// that takes a Javalin `Context` as an argument
public class DefaultPageHandler {

    private Logger logger;
    private DisplayLogic displayLogic;

    /**
     * the template that contains the top page for the Chirpy website. It's located
     * in the resources/templates/ directory.
     */
    private final String DEFAULT_PAGE = "toppage.thtml";

    public DefaultPageHandler(Logger log, DisplayLogic dl) {
        logger = log;
        displayLogic = dl;
    }

    public void handle(Context ctx) {
        logger.info("DefaultPageHandler called");

        // sw will hold the output of parsing the template
        StringWriter sw = new StringWriter();
        // dataModel will hold the data to be used in the template
        Map<String, Object> dataModel = new HashMap<String, Object>();

        {
            // I'm putting this in a code block because it's really just demo
            // code. We're populating the dataModel with some example data
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

        // now we call the display method to parse the template and write the output
        displayLogic.parseTemplate(DEFAULT_PAGE, dataModel, sw);

        // set the type of content (in this case, we're sending back HTML)
        ctx.contentType("text/html");
        ctx.result(sw.toString());
    }
}
