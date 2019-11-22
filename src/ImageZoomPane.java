import java.io.File;
import java.io.IOException;
import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

class ImageZoomPane extends JFrame implements Observer {
    private ImageModel imageModel;
    Rating rating;

    ImageZoomPane(ImageModel imageModel) {
        this.imageModel = imageModel;
        imageModel.addObserver(this);

        // frame
        setResizable(false);

        // content panel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        this.add(content);

        // picture display
        Image resizedPicture = imageModel.getImageBuffer().getScaledInstance(Constants.IMG_ZOOM_DIMENSION_WIDTH, Constants.IMG_ZOOM_DIMENSION_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon imgIcon = new ImageIcon(resizedPicture);
        JLabel picLabel = new JLabel(imgIcon);
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLabel.setBorder(new LineBorder(Color.BLACK));
        content.add(picLabel);

        // rating display
        rating = new Rating(imageModel.getRating(), r -> imageModel.setRating(r));
        content.add(rating);

        // close button
        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            ImageZoomPane.this.dispatchEvent(new WindowEvent(ImageZoomPane.this, WindowEvent.WINDOW_CLOSING));
        });
        close.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(close);

        pack();
    }
    
    @Override
    public void update() {
        rating.setRating(imageModel.getRating());
    }
}