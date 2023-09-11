/**
 * Display logic for Chirpy.
 */

package edu.georgetown.dl;

import freemarker.core.ParseException;
import freemarker.template.*;
import java.util.*;
import java.io.*;
import java.net.URLDecoder;
import java.util.logging.Logger;

public class DisplayLogic {

    private Logger logger;
    private Configuration cfg;

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

    public void display(String templateName, Map<String, Object> dataModel, Writer out) {
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

    // from
    // https://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
    public static Map<String, String> parseResponse( String query ) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
