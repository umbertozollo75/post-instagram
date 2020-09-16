package it.sparks.postinstagram.bl.service;

import it.sparks.postinstagram.entity.Post;
import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utils.InstagramUtils;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class PostService {

    private final EntityManager em;

    public PostService() {
        super();

        this.em = InstagramUtils.getManager();
    }

    public Post getPost(Long id) {
        LOG.traceIn();

        String sql = "SELECT p FROM Post p WHERE p.idPost = :id";

        Post post = (Post) this.em.createQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        LOG.traceOut();
        return post;
    }

    public Post getAllBetweenDates(Instant startDate, Instant endDate) {
        LOG.traceIn();

        String sql = "SELECT p FROM Post p WHERE p.dateStart BETWEEN :startDate AND :endDate";

        Post post = (Post) this.em.createQuery(sql)
            .setParameter("startDate", startDate.toString())
            .setParameter("endDate", endDate.toString())
            .getSingleResult();

        LOG.debug("POST: " + post.toString());

        LOG.traceOut();
        return post;
    }
}
