package it.sparks.postinstagram.main;

import it.sparks.postinstagram.bl.BusinessLogic;
import it.sparks.postinstagram.utilities.costants.InstagramConst;
import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.examples.InstagramLoginUtils;
import it.sparks.postinstagram.utilities.InstagramUtils;

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

        String POST_ALBUM = "S";
        String PATH = InstagramConst.PATH_IG;
        String componente = "orizzontali"; // "verticali"  "quadrate"

        String caption = "Evento 20 dec 2020 [1/2]" + A_CAPO;
        //caption += "Grazie: @morenobaggio @associazione_tus" + A_CAPO;
        //caption += "Model: @vielbc_ @valerialariccia__" + A_CAPO;
        caption += "Ph: @umbertozollo" + A_CAPO;
        caption +=  "." + A_CAPO;
        caption +=  "." + A_CAPO;
        caption +=  "." + A_CAPO;
        caption += "#foto #portait #portaitphotography #photoart #fotoinstudio #turingirl #model #modellaitaliana " +
                "#glamour #glamourfoto #glamourfotography #glamourphoto #glamourphotography #d750 #d750nikon #777luckyfish";

        BusinessLogic bl = new BusinessLogic();

        try {
            InstagramUtils.loadInstagramProperties();
            LOG.info("Default Locale = " + Locale.getDefault().getDisplayName());

            // Login to instagram
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

}
