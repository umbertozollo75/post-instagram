package it.sparks.postinstagram.bl;

import it.sparks.postinstagram.utilities.logging.LOG;
import it.sparks.postinstagram.utils.InstagramUtils;
import org.brunocvcunha.instagram4j.requests.InstagramEditMediaRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadAlbumRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramCarouselMediaItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureAlbumResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureMediaResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

import java.io.*;
import java.util.*;

public class BusinessLogic {

    static final String[] VALID_EXTENSION = {
        "jpg"
    };
    static final float POSITION_X_MAX;
    static final float POSITION_Y_MAX;

    static {
        POSITION_X_MAX = 0.2f;
        POSITION_Y_MAX = 0.8f;
    }

    List<Tag> coordTags = new ArrayList<>();

    public BusinessLogic() {
    }

    public void postAlbum(File folder, String caption, Properties tagImages) throws IOException {
        LOG.traceIn();

        List<File> images;
        images = getImagesInFolder(folder);
        LOG.debug("IMAGES: " + images.size());
        if (images != null) {
            InstagramUploadAlbumRequest album = new InstagramUploadAlbumRequest(images, caption);
            InstagramConfigureAlbumResult result = InstagramUtils.getInstagram4j().sendRequest(album);

            LOG.debug("RESULT: " + result);

            List<InstagramCarouselMediaItem> licmi = result.getMedia().getCarousel_media();

            int i = 0;
            for (InstagramCarouselMediaItem x : licmi) {

                String[] tags = (tagImages.getProperty(String.format("%02d", i++))).split(";");

                InstagramEditMediaRequest iemr = new InstagramEditMediaRequest(x.getPk(), "");
                InstagramEditMediaRequest.UserTags userTags = iemr.new UserTags();
                for (String tag: tags) {
                    addTag(tag, iemr, userTags);
                }
                iemr.setUserTags(userTags);
                InstagramUtils.getInstagram4j().sendRequest(iemr);
            }
        } else {
            LOG.info("NESSUNA FOTO DA CARICARE!!!");
        }
        LOG.traceOut();
    }

    public void postSingle(File folder, String caption, Properties tagImages) throws IOException {
        LOG.traceIn();

        //create a FileFilter and override its accept-method
        FileFilter jpgFilefilter = new FileFilter() {
            //Override accept method
            public boolean accept(File file) {
                //if the file extension is .log return true, else false
                if (file.getName().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }
        };

        int i = 0;
        for (File image : Objects.requireNonNull(folder.listFiles(jpgFilefilter))) {
            String nomeFile = image.getName();
            String nomeFileWithoutExt = nomeFile.substring(0, nomeFile.lastIndexOf("."));

            InstagramUploadPhotoRequest iupr = new InstagramUploadPhotoRequest(image, caption);
            InstagramConfigureMediaResult icmr = InstagramUtils.getInstagram4j().sendRequest(iupr);

            LOG.debug("UMBERTO id: " + icmr.getMedia().getId());
            LOG.debug("UMBERTO pk: " + icmr.getMedia().getPk());

            String[] tags = (tagImages.getProperty(String.format("%02d", i++))).split(";");

            InstagramEditMediaRequest iemr = new InstagramEditMediaRequest(icmr.getMedia().getPk(), "");
            InstagramEditMediaRequest.UserTags userTags = iemr.new UserTags();
            for (String tag: tags) {
                addTag(tag, iemr, userTags);
            }
            iemr.setUserTags(userTags);
            InstagramUtils.getInstagram4j().sendRequest(iemr);
        }
        LOG.traceOut();
    }

    public Properties getTag(String path, String component) {
        LOG.traceIn();

        Properties props = new Properties();
        FileInputStream fis = null;

        String file = path + File.separator + component + ".properties";
        try {
            File f = new File(file);
            fis = new FileInputStream(f);
            props.load(fis);
        } catch (FileNotFoundException fnfex) {
            LOG.error("File properties non presente: " + fnfex.getMessage(), fnfex);
        } catch (IOException ioex) {
            LOG.error("Errore lettura properties: " + ioex.getMessage(), ioex);
        } finally {

        }

        LOG.traceOut();
        return props;
    }

    /**
     *
     * @param folder
     * @return
     */
    private List<File> getImagesInFolder(File folder) {
        LOG.traceIn();

        //create a FileFilter and override its accept-method
        FileFilter jpgFilefilter = new FileFilter() {
            //Override accept method
            public boolean accept(File file) {
                //if the file extension is .log return true, else false
                if (file.getName().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }
        };

        List<File> images = new ArrayList<File>();

        int numFile = folder.listFiles(jpgFilefilter).length;
        LOG.debug("NUM_FILES: " + numFile);
        if (numFile >= 1 && numFile <= 10) {
            for (File image : folder.listFiles(jpgFilefilter)) {
                images.add(image);
            }
        } else {
            LOG.info("Numero immagini superiore a 10!");
            return null;
        }
        LOG.traceOut();

        return images;
    }

    private String getExtension(String filename) {
        LOG.traceIn();

        return filename.substring(filename.indexOf(".")+1);
    }

    private void addTag(String tag, InstagramEditMediaRequest iemr, InstagramEditMediaRequest.UserTags userTags) throws IOException {
        LOG.traceIn();

        float[] coordTag = createCoord();

        while (coordInto(coordTag)) {
            coordTag = createCoord();
        }

        Tag t = new Tag(coordTag[0], coordTag[1]);

        coordTags.add(t);

        InstagramSearchUsernameRequest user = new InstagramSearchUsernameRequest(tag.substring(1));
        InstagramSearchUsernameResult userResult = InstagramUtils.getInstagram4j().sendRequest(user);

        LOG.debug("ID for " + tag + " is " + userResult.getUser().getPk());

        InstagramEditMediaRequest.UserTag userTag = iemr.new UserTag(String.valueOf(userResult.getUser().getPk()), coordTag[0], coordTag[1]);
        userTags.getTagsToAdd().add(userTag);

        LOG.traceOut();
    }

    private float[] createCoord() {
        LOG.traceIn();

        Random rnd = new Random();

        float[] result = new float[2];

        result[0] = POSITION_X_MAX + rnd.nextFloat() * (POSITION_Y_MAX - POSITION_X_MAX);
        result[1] = POSITION_X_MAX + rnd.nextFloat() * (POSITION_Y_MAX - POSITION_X_MAX);
        LOG.debug("result0: " + result[0] + " - result1: " + result[1]);

        LOG.traceOut();
        return result;
    }

    private boolean coordInto(float[] coordTag) {
        LOG.traceIn();

        Tag t = new Tag(coordTag[0], coordTag[1]);

        for (Tag tag: coordTags) {

            if (
                (t.coordX - (t.coordX * 10 / 100))  >= tag.coordX && tag.coordX <= (t.coordX + (t.coordX * 10 / 100))
                && (t.coordY - (t.coordY * 10 / 100))  >= tag.coordY && tag.coordY <= (t.coordY + (t.coordY * 10 / 100))
            ) {
                return true;
            }
        }

        LOG.traceOut();
        return false;
    }

    class Tag {
        float coordX;
        float coordY;

        public Tag(float coordX, float coordY) {
            this.coordX = coordX;
            this.coordY = coordY;
        }
    }
}
