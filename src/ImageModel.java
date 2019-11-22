import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

class ImageModel extends Observable {
    private File imgFile;
    private BufferedImage imgBuffer;
    private int rating;
    private String dateCreated;

    ImageModel(File file, int rating) throws IOException {
        this.imgFile = file;
        this.rating = rating;

        // picture display
        BufferedImage myPicture = ImageIO.read(file);
        imgBuffer = myPicture;
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.dateCreated = dateFormat.format(attr.creationTime().toMillis());
    }
    /**
     * @return the imgFile
     */
    public File getImgFile() {
        return imgFile;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @return the dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }
    
    /**
     * @return the imgBuffer
     */
    public BufferedImage getImageBuffer() {
        return imgBuffer;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
        notifyObservers();
    }
}