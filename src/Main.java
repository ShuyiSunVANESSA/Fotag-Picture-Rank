import java.io.IOException;

import javax.swing.JOptionPane;

class Main {
    public static void main(String[] args) {
        try {
            FotagResources.init();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot load necessary resources.");
        }
        RestoreDataModel restoreModel = FotagModel.restoreModel();
        FotagPane fotagPane = new FotagPane(restoreModel.getFotagModel());
        if (!restoreModel.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(null, restoreModel.getErrorMessage());
        }
        fotagPane.setVisible(true);
    }
}