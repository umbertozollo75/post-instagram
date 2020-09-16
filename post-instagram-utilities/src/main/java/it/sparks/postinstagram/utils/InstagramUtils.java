package it.sparks.postinstagram.utils;

import it.sparks.postinstagram.utilities.logging.LOG;
import org.brunocvcunha.instagram4j.Instagram4j;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Properties;

public class InstagramUtils {

    private static Properties appProperties;

    private static EntityManager manager = null;
    private static Instagram4j instagram4j = null;

    public static Instagram4j getInstagram4j() {

        return instagram4j;
    }

    public static void setInstagram4j(Instagram4j instagram4j) {

        InstagramUtils.instagram4j = instagram4j;
    }

    public static String getProperty(String propName) {
        LOG.traceIn();

        String ans = null;
        try {
            ans = appProperties.getProperty(propName);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        LOG.traceOut();
        return ans;
    }
/*
    public static void initLogging(Class<?> c) {
        LOG.traceIn();

        Logger logger = Logger.getLogger(c.class);
        PropertyConfigurator.configure(logger.getResourceAsStream("log4j.properties"));

        LOG.traceOut();
    }
*/
    public static void loadInstagramProperties() throws IOException {
        LOG.traceIn();

        Properties props = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props.load(loader.getResourceAsStream("post-instagram.properties"));

        appProperties = props;

        LOG.traceOut();
    }

    public static void setProperty(String propName, String propValue) {
        appProperties.setProperty(propName, propValue);
    }

    public static EntityManager getManager() {

        return manager;
    }

    public static void setManager(EntityManager manager) {

        InstagramUtils.manager = manager;
    }

    public static void closeManager() {

        if (manager != null) {
            manager.close();
        }
    }

    private InstagramUtils() {

        super();
    }

}
