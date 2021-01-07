package it.sparks.postinstagram.examples;

import it.sparks.postinstagram.bl.BusinessLogic;
import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.InstagramUtils;
import it.sparks.postinstagram.utilities.logging.LOG;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

public class ProvaPersistence {

    public static void main(String[] args) {
        try {
            InstagramUtils.loadInstagramProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());

        // Login to instagram
        try {
            InstagramLoginUtils.loginInstagram();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Login effettuato...");

        BusinessLogic bl = new BusinessLogic();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postinstagramPU");
        EntityManager eman = emf.createEntityManager();

        ZoneId zone = ZoneId.of("Europe/Rome");
        LocalDateTime instant = LocalDateTime.now(zone);
        LocalDateTime instantBefore = instant.minusMinutes(5);
        LocalDateTime instantAfter = instant.plusMinutes(10);

        try {
            String sql = "SELECT * FROM post p WHERE p.dateStart BETWEEN '" + instantBefore + "' AND '" + instantAfter + "'";
            LOG.debug("SQL: " + sql);
            Query query = eman.createNativeQuery(sql, Post.class);
            List<Post> posts = query.getResultList();
            LOG.debug("SIZE: " + posts.size());
            for (Post p: posts) {
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
    }
}
