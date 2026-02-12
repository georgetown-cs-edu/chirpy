/**
 * Display logic for Chirpy.
 */

package edu.georgetown.dl;

import freemarker.core.ParseException;
import freemarker.template.*;
import java.util.*;
import java.io.*;
import java.util.logging.Logger;

import io.javalin.http.Context;

public class DisplayLogic {

    private Logger logger;
    private Configuration cfg;

    /**
     * Initializes the display logic. Specifically, it sets up the template engine.
     * You probably don't want to change much of this code, if any.
     * 
     * @param logger the logger to use
     * @throws IOException
     */
    public DisplayLogic(Logger logger) throws IOException {
        this.logger = logger;
        /* Create and adjust the configuration singleton */
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(new File("resources/templates"));
        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        this.logger.info("Disply logic initialized");
    }

    /**
     * Parses a template given the provided `dataModel`, and writes the output to
     * `out`. Templates should be stored in the resources/templates directory. You
     * probably don't want to change this function. See `DefaultPageHandler.java`
     * for an example of how to use this function.
     * 
     * @param templateName the name of the template to use
     * @param dataModel    the variables to use in the template
     * @param out          the output stream to write to
     */
    public void parseTemplate(String templateName, Map<String, Object> dataModel, Writer out) {
        Template template;
        try {
            template = cfg.getTemplate(templateName);
            template.process(dataModel, out);
        } catch (TemplateNotFoundException e) {
            logger.warning(templateName + " not found");
        } catch (MalformedTemplateNameException e) {
            logger.warning("malformed template: " + templateName);
        } catch (ParseException e) {
            logger.warning(templateName + " parse exception: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning("IO exception: " + e.getMessage());
        } catch (TemplateException e) {
            e.printStackTrace();
            logger.warning(templateName + " template exception: " + e.getMessage());
        }
    }

    /**
     * Adds a cookie to the response.
     * 
     * @param ctx the Javalin Context object representing the current request/response
     * @param var the name of the cookie
     * @param val the value of the cookie
     */
    public void addCookie(Context ctx, String var, String val) {
        ctx.cookie(var, val);
    }

    /**
     * Gets all cookies from the request.
     * 
     * @param ctx the Javalin Context object representing the current request/response
     * @return the cookies as a Map
     */
    public Map<String, String> getCookies(Context ctx) {
        return ctx.cookieMap();
    }
}
