import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.io.IOException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class FotagPane extends JFrame implements Observer {
    private FotagModel fotagModel;
    private JPanel mainPanel = new JPanel();
    private LayoutOption activeLayout = LayoutOption.GRID;
    private int ratingFilter = 0;
    private List<ImageView> imgViews = new ArrayList<>();
    private JButton gridViewButton;
    private JButton listViewButton;

    FotagPane(FotagModel fmodel) {
        this.fotagModel = fmodel;
        fmodel.addObserver(this);

        // frame
        setMinimumSize(new Dimension(400, 450));
        setPreferredSize(new Dimension(200, 500));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                try {
                    fotagModel.saveFotagModel();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(FotagPane.this, "Unable to save image information.");
                }
                System.exit(0);
            }
        });

        JPanel content = new JPanel();
        add(content);
        content.setLayout(new BorderLayout());

        // nav panel
        {
            JPanel navPanel = new JPanel();
            content.add(navPanel, BorderLayout.NORTH);
            navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
            // Grid view button
            {
                gridViewButton = new JButton(FotagResources.getGrid());
                gridViewButton.addActionListener(e -> {
                    activeLayout = LayoutOption.GRID;
                    switchView();
                });
                navPanel.add(gridViewButton);
            }
            // List view button
            {
                listViewButton = new JButton(FotagResources.getList());
                listViewButton.addActionListener(e -> {
                    activeLayout = LayoutOption.LIST;
                    switchView();
                });
                navPanel.add(listViewButton);
            }
            // App name
            {
                JLabel appName = new JLabel("Fotag");
                navPanel.add(Box.createHorizontalStrut(10));
                navPanel.add(appName);
                appName.setFont(appName.getFont().deriveFont(Font.BOLD, 25));
            }
            JButton importImageButton = new JButton(FotagResources.getLoad());
            importImageButton.addActionListener(e -> {
                JFileChooser filePicker = new JFileChooser();
                filePicker.setMultiSelectionEnabled(true);
                int success = filePicker.showOpenDialog(this);
                if (success != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                String errMsg = "";
                for (File f : filePicker.getSelectedFiles()) {
                    try {
                        fotagModel.addImg(f);
                    } catch (IOException e1) {
                        errMsg += e1.getMessage() + "\nFilename: " + f.getAbsolutePath() + "\n";
                    }
                }
                if (!errMsg.isEmpty()) {
                    JOptionPane.showMessageDialog(this, errMsg);
                }
            });

            JButton clearImgsButton = new JButton(FotagResources.getClear());
            clearImgsButton.addActionListener(e -> {
                // HACK: this is a workaround, imgView should be updated according to imgModels
                fotagModel.clearImages();
                imgViews.clear();
                mainPanel.removeAll();
            });

            navPanel.add(Box.createHorizontalGlue());
            navPanel.add(importImageButton);
            navPanel.add(clearImgsButton);
            navPanel.add(Box.createHorizontalStrut(10));

            Rating filter = new Rating(0, r -> filterImage(r));
            navPanel.add(filter);
            navPanel.add(Box.createHorizontalStrut(10));
        }

        // Scroll panel
        JScrollPane jp = new JScrollPane();
        content.add(jp, BorderLayout.CENTER);
        jp.getViewport().setBackground(UIManager.getColor("Panel.background"));
        jp.getVerticalScrollBar().setUnitIncrement(15); // speedup scrolling

        // main panel
        jp.setViewportView(mainPanel);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, Constants.IMG_H_PADDING, Constants.IMG_V_PADDING));
        switchView();
        pack();
    }

    void filterImage(int rating) {
        ratingFilter = rating;
        update();
    }

    @Override
    public void update() {
        List<ImageModel> imgModels = fotagModel.getImages();

        for (int i = imgViews.size(); i < imgModels.size(); i++) {
            ImageModel imageModel = imgModels.get(i);
            ImageView imgView = new ImageView(imageModel, activeLayout);
            mainPanel.add(imgView);
            imgViews.add(imgView);
            imageModel.addObserver(this);
        }
        for (ImageView imgView : imgViews) {
            imgView.setVisible(imgView.getImageModel().getRating() >= ratingFilter);
        }
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void switchView() {
        for (ImageView imgView : imgViews) {
            imgView.setLayout(activeLayout);
        }
        switch (activeLayout) {
            case GRID:
                mainPanel.setLayout(new WrapLayout(
                    FlowLayout.LEFT, Constants.IMG_H_PADDING, Constants.IMG_V_PADDING));
                gridViewButton.setSelected(true);
                listViewButton.setSelected(false);
                break;
            case LIST:
                mainPanel.setLayout(new GridLayout(0, 1, 20, 20));
                gridViewButton.setSelected(false);
                listViewButton.setSelected(true);
                break;
            default:
                assert false;
        }
        update();
    }
}


//TODO:
// setVisible list view not moving to the top.
// list img squeezed by window size