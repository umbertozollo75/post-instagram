package it.sparks.postinstagram.main;

import it.sparks.postinstagram.job.InstagramJob;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MyAppQuartz {

    public static void main(String[] args) {
        LOG.traceIn();

        try {
            JobDetail job1 = JobBuilder.newJob(InstagramJob.class).withIdentity("InstagramJob").build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * * * ?")).build();

            Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
            scheduler1.start();
            scheduler1.scheduleJob(job1, trigger1);
        } catch (SchedulerException sex) {
            LOG.error("" + "Errore: " + sex.getMessage(), sex);
            sex.printStackTrace();
        }

        LOG.traceOut();
    }

}
