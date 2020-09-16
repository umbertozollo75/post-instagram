package it.sparks.postinstagram.bl.service;

import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utils.InstagramUtils;

import javax.persistence.EntityManager;

public class ImageService {

    private final EntityManager em;

    public ImageService() {
        super();

        this.em = InstagramUtils.getManager();
    }

    public Image getImage(Long id) {
        LOG.traceIn();

        String sql = "SELECT i FROM Image i WHERE i.idImage = :id";

        Image image = (Image) this.em.createQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        LOG.traceOut();
        return image;
    }
}
