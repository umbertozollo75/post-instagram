package it.sparks.postinstagram.job;

import it.sparks.postinstagram.bl.BusinessLogic;
import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InstagramJob implements Job {

    public void execute(JobExecutionContext context) {
        LOG.traceIn();

        BusinessLogic bl = new BusinessLogic();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postinstagramPU");
        EntityManager eman = emf.createEntityManager();

        try {
            long now = System.currentTimeMillis();
            long before = now - TimeUnit.MINUTES.toMillis(5);
            long after = now + TimeUnit.MINUTES.toMillis(10);

            Query query = eman.createNamedQuery("Post.findPostByDate");
            query.setParameter("start", new Timestamp(before), TemporalType.TIMESTAMP);
            query.setParameter("end", new Timestamp(after), TemporalType.TIMESTAMP);

            List<Post> posts = query.getResultList();

            for (Post p : posts) {
                LOG.debug("IDPOST: " + p.getIdPost());
                LOG.debug("DESCRIZIONE: " + p.getDescrizionePost());
                LOG.debug("DATE: " + p.getDateStart());

                List<Image> images = p.getImages();
                if (p.getAlbum() == 1) {
                    bl.postAlbumDB(images, p.getDescrizionePost());
                } else {
                    bl.postSingleDB(images, p.getDescrizionePost());
                }
            }

        } catch (IOException ioex) {
            LOG.error("" + "Errore: " + ioex.getMessage(), ioex);
            ioex.printStackTrace();
        } finally {
            eman.close();
            emf.close();
        }

        LOG.traceOut();
    }
}
