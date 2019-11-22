import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rating extends JPanel implements Observer {
    private int rating;
    private RatingListener listener;
    private List<JLabel> starLabels = new ArrayList<>();

    public Rating(int rating, RatingListener listener) {
        this.listener = listener;
        
        for (int i = 0; i < Constants.NUM_OF_STARS; i++){
            final int numOfStars = i + 1;
            JLabel starLabel = new JLabel(i < rating 
                ? FotagResources.getFullStar()
                : FotagResources.getEmptyStar());
            starLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setRating(Rating.this.rating == numOfStars ? 0 : numOfStars);
                }
            });
            starLabels.add(starLabel);
            this.add(starLabel);
        }
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        if (rating == this.rating) {
            return;
        }
        this.rating = rating;
        // clicking the same star would set filter to zero
        for (int i = 0; i < Constants.NUM_OF_STARS; i++){
            starLabels.get(i).setIcon(i < rating
                ? FotagResources.getFullStar()
                : FotagResources.getEmptyStar());
        }
        listener.ratingChanged(rating);
        this.repaint();
        this.revalidate();
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }
}