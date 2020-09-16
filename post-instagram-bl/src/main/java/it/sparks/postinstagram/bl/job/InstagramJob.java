package it.sparks.postinstagram.bl.job;

import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class InstagramJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.traceIn();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // Convert timestamp to instant
        Instant instant = timestamp.toInstant();

        Duration duration5 = Duration.ofMinutes(2);
        Duration duration25 = Duration.ofMinutes(2);
        Instant instantMinuteBefore = instant.minus(duration5);
        Instant instantMinuteAfter = instant.plus(duration25);

        EventJob ej = new EventJob();
        Post p = ej.leggiDb(instantMinuteBefore, instantMinuteAfter);

        LOG.debug("POST: " + p.toString());

        LOG.traceOut();
    }
}
