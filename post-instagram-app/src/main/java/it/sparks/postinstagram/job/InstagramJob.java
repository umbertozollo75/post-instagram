package it.sparks.postinstagram.job;

import it.sparks.postinstagram.bl.BusinessLogic;
import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class InstagramJob implements Job {

    public void execute(JobExecutionContext context) {
        LOG.traceIn();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postinstagramPU");
        EntityManager eman = emf.createEntityManager();

        ZoneId zone = ZoneId.of("Europe/Rome");
        LocalDateTime instant = LocalDateTime.now(zone);
        LocalDateTime instantBefore = instant.minusMinutes(5);
        LocalDateTime instantAfter = instant.plusMinutes(10);

        try {
            String sql = "SELECT * FROM post p WHERE p.dateStart BETWEEN '" + instantBefore + "' AND '" + instantAfter + "'";
            LOG.info("SQL: " + sql);
            Query query = eman.createNativeQuery(sql, Post.class);
            List<Post> posts = query.getResultList();
            LOG.debug("SIZE: " + posts.size());
            for (Post p: posts) {
                LOG.debug("IDPOST: " + p.getIdPost());
                LOG.debug("DESCRIZIONE: " + p.getDescrizionePost());
                LOG.debug("DATE: " + p.getDateStart());

                BusinessLogic bl = new BusinessLogic();

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
