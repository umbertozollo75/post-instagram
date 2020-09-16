package it.sparks.postinstagram.bl.job;

import it.sparks.postinstagram.bl.service.ImageService;
import it.sparks.postinstagram.bl.service.PostService;
import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.logging.LOG;

import java.time.Instant;

public class EventJob {

    public Post leggiDb(Instant before, Instant after) {
        LOG.traceIn();

        PostService ps = new PostService();
        Post p = ps.getAllBetweenDates(before, after);

        LOG.debug("POST: " + p.toString());

        LOG.traceOut();
        return p;
    }
}
