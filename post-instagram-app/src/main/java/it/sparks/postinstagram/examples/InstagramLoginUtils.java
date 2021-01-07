package it.sparks.postinstagram.examples;

import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utilities.InstagramUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramResetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSelectVerifyMethodRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSendSecurityCodeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetChallengeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSelectVerifyMethodResult;

import java.util.Objects;
import java.util.Scanner;

public class InstagramLoginUtils {

    public static void loginInstagram() throws Exception {
        String username = InstagramUtils.getProperty("instagram.user");
        String password = InstagramUtils.getProperty("instagram.pw");

        // Login to instagram
        Instagram4j instagram4j = Instagram4j.builder().username(username).password(password).build();
        instagram4j.setup();
        // Get login response
        InstagramLoginResult instagramLoginResult = instagram4j.login();
        // Check login response
        checkInstagramLoginResult(instagram4j, instagramLoginResult, true);

        InstagramUtils.setInstagram4j(instagram4j);
    }
    /**
     * Check login response.
     *
     * @param instagram4j
     * @param instagramLoginResult
     * @param doReAuthentication
     * @throws Exception
     */
    private static void checkInstagramLoginResult(Instagram4j instagram4j, InstagramLoginResult instagramLoginResult, boolean doReAuthentication) throws Exception {
        LOG.traceIn();
        
        if (Objects.equals(instagramLoginResult.getStatus(), "ok") && instagramLoginResult.getLogged_in_user() != null) {
            // Login success
            LOG.debug("Login success.");
            LOG.debug(instagramLoginResult.getLogged_in_user().toString());
        } else if (Objects.equals(instagramLoginResult.getStatus(), "ok")) {
            // Logged in user not exists
            LOG.debug("Logged in user not exists.");
            // TODO
        } else if (Objects.equals(instagramLoginResult.getStatus(), "fail")) {
            // Login failed
            // Check error type
            if (Objects.equals(instagramLoginResult.getError_type(), "checkpoint_challenge_required")) {
                LOG.debug("Challenge URL : " + instagramLoginResult.getChallenge().getUrl());
                // If do re-authentication
                if (doReAuthentication) {
                    // Get challenge URL
                    String challengeUrl = instagramLoginResult.getChallenge().getApi_path().substring(1);
                    // Reset
                    String resetUrl = challengeUrl.replace("challenge", "challenge/reset");
                    InstagramGetChallengeResult getChallengeResult = instagram4j.sendRequest(new InstagramResetChallengeRequest(resetUrl));
                    LOG.debug("Reset result : " + getChallengeResult);
                    // if "close"
                    if (Objects.equals(getChallengeResult.getAction(), "close")) {
                        // Get challenge response
                        getChallengeResult = instagram4j.sendRequest(new InstagramGetChallengeRequest(challengeUrl));
                        LOG.debug("Challenge response : " + getChallengeResult);
                    }
                    // Check step name
                    if (Objects.equals(getChallengeResult.getStep_name(), "select_verify_method")) {
                        // Select verify method
                        // Get select verify method result
                        InstagramSelectVerifyMethodResult postChallengeResult = instagram4j.sendRequest(new InstagramSelectVerifyMethodRequest(challengeUrl, getChallengeResult.getStep_data().getChoice()));
                        // If "close"
                        if (Objects.equals(postChallengeResult.getAction(), "close")) {
                            // Challenge was closed
                            LOG.debug("Challenge was closed : " + postChallengeResult);
                            // End
                            return;
                        }
                        // Security code has been sent
                        LOG.info("Security code has been sent : " + postChallengeResult);
                        // Please input Security code
                        LOG.info("Please input Security code: ");
                        String securityCode = null;
                        try (Scanner scanner = new Scanner(System.in)) {
                            securityCode = scanner.nextLine();
                        }
                        // Send security code
                        InstagramLoginResult securityCodeInstagramLoginResult = instagram4j.sendRequest(new InstagramSendSecurityCodeRequest(challengeUrl, securityCode));
                        // Check login response
                        checkInstagramLoginResult(instagram4j, securityCodeInstagramLoginResult, false);
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "verify_email")) {
                        // Security code has been sent to E-mail
                        LOG.debug("Security code has been sent to E-mail");
                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "verify_code")) {
                        // Security code has been sent to phone
                        LOG.debug("Security code has been sent to phone");
                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "submit_phone")) {
                        // Unknown
                        LOG.debug("Unknown.");
                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "delta_login_review")) {
                        // Maybe showing security confirmation view?
                        LOG.debug("Maybe showing security confirmation view?");
                        // FIXME Send request with choice
                        InstagramSelectVerifyMethodResult instagramSelectVerifyMethodResult = instagram4j.sendRequest(new InstagramSelectVerifyMethodRequest(challengeUrl, getChallengeResult.getStep_data().getChoice()));
                        LOG.debug(instagramSelectVerifyMethodResult.toString());
                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "change_password")) {
                        // Change password needed
                        LOG.debug("Change password needed.");
                    } else if (Objects.equals(getChallengeResult.getAction(), "close")) {
                        // Maybe already challenge closed at other device
                        LOG.debug("Maybe already challenge closed at other device.");
                        // TODO
                    } else {
                        // TODO Other
                        LOG.debug("Other.");
                    }
                }
            } else if (Objects.equals(instagramLoginResult.getError_type(), "bad_password")) {
                LOG.debug("Bad password.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "rate_limit_error")) {
                LOG.debug("Too many request.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "invalid_parameters")) {
                LOG.debug("Invalid parameter.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "needs_upgrade")) {
                LOG.debug("App upgrade needed.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "sentry_block")) {
                LOG.debug("Sentry block.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "inactive user")) {
                LOG.debug("Inactive user.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "unusable_password")) {
                LOG.debug("Unusable password.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (instagramLoginResult.getTwo_factor_info() != null) {
                LOG.debug("Two factor authentication needed.");
                LOG.debug(instagramLoginResult.getMessage());
                // If do re-authentication
                if (doReAuthentication) {
                    // Two factor authentication
                    InstagramLoginResult twoFactorInstagramLoginResult = instagram4j.login();   //VERIFICATION_CODE
                    // Check login result
                    checkInstagramLoginResult(instagram4j, twoFactorInstagramLoginResult, false);
                }
            } else if (Objects.equals(instagramLoginResult.getMessage(), "Please check the code we sent you and try again.")) {
                LOG.debug("Invalid security code.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getMessage(),"This code has expired. Go back to request a new one.")) {
                LOG.debug("Security code has expired.");
                LOG.debug(instagramLoginResult.getMessage());
            } else if (instagramLoginResult.getChallenge() != null) {
                LOG.debug("Challenge : " + instagramLoginResult.getChallenge());
                LOG.debug(instagramLoginResult.getMessage());
                if (instagramLoginResult.getChallenge().getLock() != null && instagramLoginResult.getChallenge().getLock()) {
                    // Login locked
                    LOG.debug("Login locked.");
                } else {
                    // Logged in user exists, or maybe showing security code
                    // view on other device
                    LOG.debug("Logged in user exists, or maybe showing security code view on other device.");
                }
            } else {
                LOG.debug("Unknown error : " + instagramLoginResult.getError_type());
                LOG.debug(instagramLoginResult.getMessage());
            }
        } else {
            LOG.debug("Unknown status : " + instagramLoginResult.getStatus());
            LOG.debug(instagramLoginResult.getMessage());
        }
        
        LOG.traceOut();
    }
}
