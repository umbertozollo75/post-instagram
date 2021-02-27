package it.sparks.postfacebook.main;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import it.sparks.utilities.constants.FacebookConstants;
import it.sparks.utilities.logging.LOG;

import java.net.MalformedURLException;


public class PostOnFacebook {

    public static void main(String[] args) throws FacebookException, MalformedURLException {
        LOG.traceIn();

        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(FacebookConstants.MY_APP_ID, FacebookConstants.MY_APP_SECRET);
        facebook.setOAuthPermissions("id, email, user_posts, publish_to_groups, pages_read_engagement, pages_manage_posts");
        facebook.setOAuthAccessToken(new AccessToken(FacebookConstants.MY_ACCESS_TOKEN, null));

        facebook.postStatusMessage("Hello World from Facebook4J.");

        LOG.traceOut();
    }

}
