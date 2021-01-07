package it.sparks.postinstagram.examples;

import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utilities.InstagramUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramEditMediaRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PostOnInstagramTag {
    static final Long IG_PK_IMAGE = 2357322843957981883L;

    public static void main(String[] args) {

        initializeProperties();

        // Login to instagram
        try {
            loginInstagram();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("LOGIN");

        float min = 0.0f;
        float max = 1.0f;
        Random rnd = new Random();

        float random1 = min + rnd.nextFloat() * (max - min);
        float random2 = min + rnd.nextFloat() * (max - min);
        LOG.info("Random1: " + random1 + " - Random2: " + random2);
        try {
            InstagramEditMediaRequest r = new InstagramEditMediaRequest(IG_PK_IMAGE, "");
            InstagramEditMediaRequest.UserTags tag = r.new UserTags();
            InstagramEditMediaRequest.UserTag ut = r.new UserTag("893787266", random1, random2);
            tag.getTagsToAdd().add(ut);
            //tag.getUserIdsToRemoveTag().add(null);
            r.setUserTags(tag);
            InstagramUtils.getInstagram4j().sendRequest(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeProperties() {
        LOG.traceIn();

        try {
            InstagramUtils.loadInstagramProperties();
            //InstagramUtils.initLogging();

            LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());

            long now = System.currentTimeMillis();
            InstagramUtils.setProperty("START.MS", Long.toString(now));
            Date today = new Date(now);

            InstagramUtils.setProperty("START.YMD", new SimpleDateFormat("yyyyMMdd").format(today));
            InstagramUtils.setProperty("START.HMS", new SimpleDateFormat("HHmmss").format(today));
        } catch(Exception  e) {
            e.printStackTrace();
        } finally {
            LOG.traceOut();
        }
    }

    private static void loginInstagram() throws IOException {
        // Login to instagram
        InstagramUtils.setInstagram4j(Instagram4j.builder().username(InstagramUtils.getProperty("instagram.user")).password(InstagramUtils.getProperty("instagram.pw")).build());
        InstagramUtils.getInstagram4j().setup();
        InstagramUtils.getInstagram4j().login();
    }

}
