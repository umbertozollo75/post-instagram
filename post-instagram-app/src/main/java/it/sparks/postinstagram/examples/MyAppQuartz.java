package it.sparks.postinstagram.examples;

import it.sparks.postinstagram.bl.job.InstagramJob;
import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utils.InstagramUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.HashMap;
import java.util.Map;

public class MyAppQuartz {

    @PersistenceUnit(name="PostInstagramPU")
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        LOG.traceIn();

        try {
            InstagramUtils.loadInstagramProperties();
            //InstagramUtils.initLogging();

            factory = Persistence.createEntityManagerFactory("PostInstagramPU", getJpaPropertyMap());
            EntityManager em = factory.createEntityManager();

            InstagramUtils.setManager(em);

            JobDetail job1 = JobBuilder.newJob(InstagramJob.class).withIdentity("InstagramJob").build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")).build();

            Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
            scheduler1.start();
            scheduler1.scheduleJob(job1, trigger1);
        } catch(Exception ex) {
            LOG.error("" + "Errore collegamento db: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }

        LOG.traceOut();
    }

    private static Map<String, String> getJpaPropertyMap() {
        LOG.traceIn();

        Map<String, String> properties = new HashMap<String, String>();

        properties.put("javax.persistence.jdbc.driver", InstagramUtils.getProperty("javax.persistence.jdbc.driver"));
        properties.put("javax.persistence.jdbc.url", InstagramUtils.getProperty("javax.persistence.jdbc.url"));
        properties.put("javax.persistence.jdbc.user", InstagramUtils.getProperty("javax.persistence.jdbc.user"));
        properties.put("javax.persistence.jdbc.password", InstagramUtils.getProperty("javax.persistence.jdbc.password"));

        LOG.traceOut();
        return properties;
    }
}
