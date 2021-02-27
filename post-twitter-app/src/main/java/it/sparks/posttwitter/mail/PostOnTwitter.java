package it.sparks.posttwitter.mail;

import it.sparks.utilities.logging.LOG;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class PostOnTwitter {

    public static void main(String args[]) throws TwitterException {
        LOG.traceIn();

        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        Status status = twitter.updateStatus("creating baeldung API");
        LOG.info("Successfully updated the status to [" + status.getText() + "].");

        LOG.traceOut();
    }
}
