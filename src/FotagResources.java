import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class FotagResources {
    private static ImageIcon fullStar;
    private static ImageIcon emptyStar;
    private static ImageIcon load;
    private static ImageIcon grid;
    private static ImageIcon list;
    private static ImageIcon clear;

    public static void init() throws IOException {
        fullStar = loadIcon("full.png", Constants.SIZE_OF_RATING_STARS);
        emptyStar = loadIcon("empty.png", Constants.SIZE_OF_RATING_STARS);
        load = loadIcon("load.png", Constants.SIZE_OF_LOAD_ICON);
        grid = loadIcon("grid.png", Constants.SIZE_OF_LAYOUT_ICON);
        list = loadIcon("list.png", Constants.SIZE_OF_LAYOUT_ICON);
        clear = loadIcon("clear.png", Constants.SIZE_OF_CLEAR_ICON);        
    }

    private static ImageIcon loadIcon(String iconName, int size) throws IOException {
        BufferedImage bufferImg = ImageIO.read(new File(iconName));
        return new ImageIcon(bufferImg.getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    /**
     * @return the emptyStar
     */
    public static ImageIcon getEmptyStar() {
        return emptyStar;
    }

    /**
     * @return the fullStar
     */
    public static ImageIcon getFullStar() {
        return fullStar;
    }
    /**
     * @return the grid
     */
    public static ImageIcon getGrid() {
        return grid;
    }
    /**
     * @return the list
     */
    public static ImageIcon getList() {
        return list;
    }
    /**
     * @return the load
     */
    public static ImageIcon getLoad() {
        return load;
    }
    /**
     * @return the clear
     */
    public static ImageIcon getClear() {
        return clear;
    }
}