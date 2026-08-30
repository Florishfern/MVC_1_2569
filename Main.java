import controller.ElectionController;
import model.ElectionModel;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            ElectionModel model = new ElectionModel();

            DataSeeder.seed(model);

            ElectionController controller = new ElectionController(model);

            MainFrame view = new MainFrame(model, controller);

            controller.setView(view);

            controller.start();
        });
    }
}
