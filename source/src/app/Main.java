package app;
import javax.swing.SwingUtilities;
import gui.LoginFrame;
import gui.AdminPanel;
public class Main {
    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
