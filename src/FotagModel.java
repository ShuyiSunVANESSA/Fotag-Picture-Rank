import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


class FotagModel extends Observable {
    List<ImageModel> images = new ArrayList<>();

    // addImg return true if img is added
    public ImageModel addImg(File imgFile) throws IOException {
        ImageModel img = new ImageModel(imgFile, 0);
        this.images.add(img);
        notifyObservers();
        return img;
    }

    /**
     * @return the images
     */
    public List<ImageModel> getImages() {
        return images;
    }

    public static RestoreDataModel restoreModel() {
        FotagModel fotagModel = new FotagModel();
        String errMsg = "";

        try (FileReader fr = new FileReader(Constants.MODEL_DATA_PATH)) {
            BufferedReader reader = new BufferedReader(fr);
            String textLine;

            while((textLine=reader.readLine()) != null) {
                String[] imgInfo = textLine.split(" ", 2);
                int rating = Integer.parseInt(imgInfo[0]);
                File imgFile = new File(imgInfo[1]);
                try {
                    ImageModel imgModel = new ImageModel(imgFile, rating);
                    fotagModel.images.add(imgModel);
                } catch (IOException e1) {
                    errMsg += e1.getMessage() + "\nFilename: " + imgFile.getAbsolutePath() + "\n";
                }
            }
        } catch (IOException e) {}
        return new RestoreDataModel(fotagModel, errMsg);           
    }

    public void saveFotagModel() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.MODEL_DATA_PATH))) {
            for (ImageModel image : images) {
                writer.write(Integer.toString(image.getRating()));
                writer.write(' ');
                writer.write(image.getImgFile().getAbsolutePath());
                writer.write('\n');
            }
        } 
    }

	public void clearImages() {
        images.clear();
        notifyObservers();
	}

}