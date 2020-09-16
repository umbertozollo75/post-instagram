package it.sparks.postinstagram.main;

import it.sparks.postinstagram.bl.BusinessLogic;
import it.sparks.postinstagram.utilities.costants.InstagramConst;
import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.bl.InstagramLoginUtils;
import it.sparks.postinstagram.utils.InstagramUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostOnInstagram {
    static final String A_CAPO = "\n";
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        LOG.traceIn();

        //Logger.getLogger("org.brunocvcunha.instagram4j").setLevel(Level.OFF);

        String POST_ALBUM = "N";
        String PATH = InstagramConst.PATH_IG;
        String componente = "verticali"; // "orizzontali" "quadrate"

        String caption = "Giornata GLAMOUR..." + A_CAPO;
        //caption += "Grazie: @associazione_tus" + A_CAPO;
        //caption += "Model: @celeste_2893" + A_CAPO;
        caption += "Ph: @umbertozollo" + A_CAPO;
        caption +=  "." + A_CAPO;
        caption +=  "." + A_CAPO;
        caption +=  "." + A_CAPO;
        caption +=  "." + A_CAPO;
        caption += "#foto #portait #portaitphotography #photoart #fotoinstudio #turingirl #model #modellaitaliana " +
                "#glamour #glamourfoto #glamourfotography #glamourphoto #glamourphotography";

        BusinessLogic bl = new BusinessLogic();

        // Login to instagram
        try {
            initializeProperties();

            InstagramLoginUtils.loginInstagram();
            LOG.info("Login effettuato...");

            File folder = new File(PATH + File.separator + componente);
            Properties tagImages = bl.getTag(PATH, componente);

            if (POST_ALBUM.matches("S")) {
                bl.postAlbum(folder, caption, tagImages);
            } else if (POST_ALBUM.matches("N")) {
                bl.postSingle(folder, caption, tagImages);
            }
        } catch (IOException ioex) {
            LOG.error("" + ioex.getMessage(), ioex);
        } catch (Exception ex) {
            LOG.error("" + ex.getMessage(), ex);
        } finally {
            LOG.traceOut();
        }
    }

    private static void initializeProperties() throws IOException {
        LOG.traceIn();

        InstagramUtils.loadInstagramProperties();
        LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());
/*
        long now = System.currentTimeMillis();
        InstagramUtils.setProperty("START.MS", Long.toString(now));
        Date today = new Date(now);

        InstagramUtils.setProperty("START.YMD", new SimpleDateFormat("yyyyMMdd").format(today));
        InstagramUtils.setProperty("START.HMS", new SimpleDateFormat("HHmmss").format(today));
 */
        LOG.traceOut();
    }

}
