package it.sparks.postinstagram.bl;

import it.sparks.postinstagram.entity.Image;
import it.sparks.postinstagram.examples.InstagramLoginUtils;
import it.sparks.postinstagram.utilities.InstagramUtils;
import it.sparks.postinstagram.utilities.logging.LOG;
import org.apache.commons.io.FileUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramEditMediaRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadAlbumRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramCarouselMediaItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureAlbumResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureMediaResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BusinessLogic {

    static final float POSITION_X_MAX;
    static final float POSITION_Y_MAX;

    static {
        POSITION_X_MAX = 0.2f;
        POSITION_Y_MAX = 0.8f;
    }

    List<Tag> coordTags = new ArrayList<>();

    static final String TMP_PATH = "c:/Temp";

    public BusinessLogic() {
    }

    public void postAlbumDB(List<Image> images, String caption) throws IOException {
        LOG.traceIn();

        Instagram4j instagram4j = InstagramUtils.getInstagram4j();

        for (Image i: images) {
            writeImageIntoTmpPath(i);
        }

        List<File> fileImages = getImagesInFolder(new File(TMP_PATH));
        LOG.debug("IMAGES: " + images.size());

        if (fileImages != null) {
            InstagramUploadAlbumRequest album = new InstagramUploadAlbumRequest(fileImages, caption);
            InstagramConfigureAlbumResult result = instagram4j.sendRequest(album);
            LOG.debug("RESULT: " + result);

            List<InstagramCarouselMediaItem> licmi = result.getMedia().getCarousel_media();
            for (int i = 0; i < licmi.size(); i++) {

                InstagramEditMediaRequest iemr = new InstagramEditMediaRequest(licmi.get(i).getPk(), "");

                List<String> stringTags = new ArrayList<>();
                List<it.sparks.postinstagram.entity.Tag> tags = images.get(i).getTags();
                for (it.sparks.postinstagram.entity.Tag t : tags) {
                    stringTags.add(t.getTag());
                }
                InstagramEditMediaRequest.UserTags userTags = iemr.new UserTags();
                for (String tag : stringTags) {
                    addTag(tag, iemr, userTags);
                }
                iemr.setUserTags(userTags);

                instagram4j.sendRequest(iemr);
            }

            Files.walk(Paths.get(TMP_PATH))
                .filter(Files::isRegularFile)
                .filter(f -> f.toString().endsWith(".jpg"))
                .map(Path::toFile)
                .forEach(File::delete);
        }

        LOG.traceOut();
    }

    public void postSingleDB(List<Image> images, String caption) throws IOException {
        LOG.traceIn();

        Instagram4j instagram4j = InstagramUtils.getInstagram4j();

        for (Image i: images) {
            InstagramUploadPhotoRequest iupr = new InstagramUploadPhotoRequest(writeImageIntoTmpPath(i), caption);
            InstagramConfigureMediaResult icmr = instagram4j.sendRequest(iupr);

            List<String> stringTags = new ArrayList<>();
            List<it.sparks.postinstagram.entity.Tag> tags = i.getTags();
            for (it.sparks.postinstagram.entity.Tag t: tags) {
                stringTags.add(t.getTag());
            }
            InstagramEditMediaRequest iemr = new InstagramEditMediaRequest(icmr.getMedia().getPk(), "");
            InstagramEditMediaRequest.UserTags userTags = iemr.new UserTags();
            for (String tag: stringTags) {
                addTag(tag, iemr, userTags);
            }
            iemr.setUserTags(userTags);

            instagram4j.sendRequest(iemr);
        }

        Files.walk(Paths.get(TMP_PATH))
                .filter(Files::isRegularFile)
                .filter(f -> f.toString().endsWith(".jpg"))
                .map(Path::toFile)
                .forEach(File::delete);

        LOG.traceOut();
    }

    public void postAlbum(File folder, String caption, Properties tagImages) throws IOException {
        LOG.traceIn();

        List<File> images ;
        images = getImagesInFolder(folder);
        if (images != null) {
            LOG.debug("IMAGES: " + images.size());
            InstagramUploadAlbumRequest album = new InstagramUploadAlbumRequest(images, caption);
            InstagramConfigureAlbumResult result = InstagramUtils.getInstagram4j().sendRequest(album);
            LOG.debug("RESULT: " + result);
            List<InstagramCarouselMediaItem> licmi = result.getMedia().getCarousel_media();
            int i = 0;
            for (InstagramCarouselMediaItem x : licmi) {

                String[] tags = (tagImages.getProperty(String.format("%02d", i++))).split(";");

                InstagramEditMediaRequest iemr = new InstagramEditMediaRequest(x.getPk(), "");
                InstagramEditMediaRequest.UserTags userTags = iemr.new UserTags();
                for (String tag : tags) {
                    addTag(tag, iemr, userTags);
                }
                iemr.setUserTags(userTags);
                InstagramUtils.getInstagram4j().sendRequest(iemr);
            }
        }

        LOG.traceOut();
    }

    public void postSingle(File folder, String caption, Properties tagImages) throws IOException {
        LOG.traceIn();

        FileFilter jpgFilefilter;
        jpgFilefilter = file -> {
            //if the file extension is .log return true, else false
            if (file.getName().endsWith(".jpg")) {
                return true;
            }
            return false;
        };

        int i = 0;
        for (File image : Objects.requireNonNull(folder.listFiles(jpgFilefilter))) {

            InstagramUploadPhotoRequest iupr = new InstagramUploadPhotoRequest(image, caption);
            Instagram4j instagram4j = InstagramUtils.getInstagram4j();
            InstagramConfigureMediaResult icmr = instagram4j.sendRequest(iupr);

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
            try {
                fis.close();
            } catch (IOException ioex) {
                LOG.error("Errore chiusura properties: " + ioex.getMessage(), ioex);
            }
        }

        LOG.traceOut();
        return props;
    }

    private File writeImageIntoTmpPath(Image image) throws IOException {
        LOG.traceIn();

        File fileImage = new File(TMP_PATH + File.separator + image.getOriginalNameFile());

        InputStream is = new ByteArrayInputStream(image.getImageBinary());
        BufferedImage img = ImageIO.read(is);
        ImageIO.write(img, "jpg", fileImage);

        LOG.traceOut();
        return fileImage;
    }

    private List<File> getImagesInFolder(File folder) {
        LOG.traceIn();

        //create a FileFilter and override its accept-method
        FileFilter jpgFilefilter;
        //Override accept method
        jpgFilefilter = file -> file.getName().endsWith(".jpg");

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

            if (tag.coordX >= (t.coordX - (t.coordX * 10 / 100))  && tag.coordX <= (t.coordX + (t.coordX * 10 / 100))
                && tag.coordY >= (t.coordY - (t.coordY * 10 / 100))  && tag.coordY <= (t.coordY + (t.coordY * 10 / 100))) {
                return true;
            }
        }

        LOG.traceOut();
        return false;
    }

}
