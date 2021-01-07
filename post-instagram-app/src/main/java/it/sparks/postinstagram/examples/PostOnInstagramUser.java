package it.sparks.postinstagram.examples;

import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utilities.InstagramUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostOnInstagramUser {

    public static void main(String[] args) {

        initializeProperties();

        // Login to instagram
        try {
            loginInstagram();
            LOG.debug("LOGIN");

            InstagramSearchUsernameRequest user = new InstagramSearchUsernameRequest("umbertozollo");
            InstagramSearchUsernameResult userResult = InstagramUtils.getInstagram4j().sendRequest(user);

            LOG.info("ID for @umbertozollo is " + userResult.getUser().getPk());
            LOG.info("Number of followers: " + userResult.getUser().getFollower_count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeProperties() {
        LOG.traceIn();

        try {
            InstagramUtils.loadInstagramProperties();

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
