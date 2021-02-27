package it.sparks.postdb.utils;

import javax.persistence.EntityManager;

public class DbUtils {

    private static EntityManager em;

    public static EntityManager getEm() {
        return em;
    }

    public static void setEm(EntityManager em) {
        DbUtils.em = em;
    }
}
