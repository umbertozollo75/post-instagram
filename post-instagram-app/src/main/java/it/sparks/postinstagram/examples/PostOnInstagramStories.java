package it.sparks.postinstagram.examples;

import it.sparks.utilities.InstagramUtils;
import it.sparks.utilities.logging.LOG;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadStoryPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureStoryResult;
import org.brunocvcunha.instagram4j.storymetadata.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostOnInstagramStories {

    public static void main(String[] args) {

        try {
            InstagramUtils.loadInstagramProperties();
            LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());

            // Login to instagram
            InstagramLoginUtils.loginInstagram();
            LOG.info("Login effettuato...");

            File img = new File("C://Temp//_DSC3522-Modifica.jpg");

            Collection<StoryMetadata> metadatas = new ArrayList<>();

            StoryHashtag hashtag = StoryHashtag.builder()
                    .tag_name("umbertozollo") //tag without '#'
                    .build();
            metadatas.add(hashtag);

            ReelMention rmention = ReelMention.builder()
                    .user_id("893787266") //pk
                    .build();
            StoryMention mention = StoryMention.builder()
                    .reel_mentions(Arrays.asList(rmention)) //all ReelMention
                    .build();
            metadatas.add(mention);

            StoryPoll poll = StoryPoll.builder()
                    .question("Ti piace?") //Note: won't actually draw question to image. Only poll sticker will appear
                    .tallies(Arrays.asList(Tally.YES, Tally.NO)) //Only 2 tallies per story poll
                    .x(0.5) //center range 0.0 - 1.0
                    .y(0.5) //center range 0.0 - 1.0
                    .width(1.0) //range 0.0 - 1.0
                    .height(1.0) //range 0.0 - 1.0
                    .build();
            metadatas.add(poll);

            InstagramUploadStoryPhotoRequest req = new InstagramUploadStoryPhotoRequest(img, metadatas);
//            InstagramUploadStoryPhotoRequest req = new InstagramUploadStoryPhotoRequest(img);

            InstagramConfigureStoryResult result = InstagramUtils.getInstagram4j().sendRequest(req);
            LOG.info("RESULT: " + result);

        } catch (Exception ex) {
            LOG.error("" + ex.getMessage(), ex);
        } finally {
            LOG.traceOut();
        }
    }

}
