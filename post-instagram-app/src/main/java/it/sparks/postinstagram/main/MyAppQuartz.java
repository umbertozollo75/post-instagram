package it.sparks.postinstagram.main;

import it.sparks.postinstagram.examples.InstagramLoginUtils;
import it.sparks.postinstagram.job.InstagramJob;
import it.sparks.postinstagram.utilities.InstagramUtils;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.Locale;

public class MyAppQuartz {

    public static void main(String[] args) {
        LOG.traceIn();

        try {
            InstagramUtils.loadInstagramProperties();
            LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());

            // Login to instagram
            InstagramLoginUtils.loginInstagram();
            LOG.info("Login effettuato...");

            JobDetail job1 = JobBuilder.newJob(InstagramJob.class).withIdentity("InstagramJob").build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")).build();

            Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
            scheduler1.start();
            scheduler1.scheduleJob(job1, trigger1);
        } catch (SchedulerException sex) {
            LOG.error("" + "Errore: " + sex.getMessage(), sex);
            sex.printStackTrace();
        } catch (IOException ioex) {
            LOG.error("" + "Errore: " + ioex.getMessage(), ioex);
            ioex.printStackTrace();
        } catch (Exception ex) {
            LOG.error("" + "Errore: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }

        LOG.traceOut();
    }

}
