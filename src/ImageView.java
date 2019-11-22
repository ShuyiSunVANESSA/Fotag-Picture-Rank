import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.Files;


public class ImageView extends JPanel implements Observer {
    private ImageModel imgModel;
    private LayoutOption activeLayout;
    private JLabel picLabel;
    private JLabel nameLabel;
    private JLabel timeLabel;
    private Rating rating;

    ImageView(ImageModel imodel, LayoutOption activeLayout) {
        this.imgModel = imodel;
        imodel.addObserver(this);

        Image resizedPicture = imodel.getImageBuffer().getScaledInstance(
            Constants.SIZE_OF_PICTURE, Constants.SIZE_OF_PICTURE, Image.SCALE_SMOOTH);
        ImageIcon imgIcon = new ImageIcon(resizedPicture);
        picLabel = new JLabel(imgIcon);
        picLabel.setBorder(new LineBorder(Color.BLACK));
        picLabel.setPreferredSize(new Dimension(200,200));

        // picture zoom in
        picLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImageZoomPane imgZoomPane = new ImageZoomPane(imodel);
                imgZoomPane.setVisible(true);
            }
        });
        this.add(picLabel);

        // file name
        File img = imodel.getImgFile();
        nameLabel = new JLabel(img.getName());

        // time label
        timeLabel = new JLabel(imodel.getDateCreated());

        // rating label
        rating = new Rating(imodel.getRating(), r -> imodel.setRating(r));
        imodel.addObserver(rating);

        setLayout(activeLayout);
    }

    @Override
    public void update() {
        rating.setRating(imgModel.getRating());
        this.repaint();
        this.revalidate();
    }

    /**
     * @return the imodel
     */
    public ImageModel getImageModel() {
        return imgModel;
    }

    public void setLayout(LayoutOption newLayout) {
        this.activeLayout = newLayout;
        this.removeAll();
        this.add(picLabel);

        switch (activeLayout) {
            case GRID:
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                this.add(nameLabel);
                JPanel dateAndRatingPanel = new JPanel();
                dateAndRatingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                dateAndRatingPanel.setLayout(new BoxLayout(dateAndRatingPanel, BoxLayout.X_AXIS));
                dateAndRatingPanel.add(timeLabel);
                dateAndRatingPanel.add(Box.createHorizontalGlue());
                dateAndRatingPanel.add(rating);
                this.add(dateAndRatingPanel);
                break;
            case LIST:
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                this.add(Box.createHorizontalStrut(50));
                JPanel imgInfo = new JPanel();
                imgInfo.setLayout(new BoxLayout(imgInfo, BoxLayout.Y_AXIS));
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                rating.setAlignmentX(Component.LEFT_ALIGNMENT);
                imgInfo.add(nameLabel);    
                imgInfo.add(timeLabel);
                imgInfo.add(rating);
                this.add(imgInfo);
                break;
            default:
                assert false;
        }
    }

}