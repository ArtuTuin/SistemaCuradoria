package gui;
import javax.swing.*;
import java.awt.*;
import model.User;
public class MainFrame extends JFrame {
    private User user;
    public MainFrame(User u) {
        this.user = u;
        setTitle("Curadoria - Principal ("+u.getTipo()+")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }
    private void init() {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem logout = new JMenuItem("Sair");
        logout.addActionListener(e -> { dispose(); new LoginFrame(); });
        menu.add(logout);
        mb.add(menu);
        setJMenuBar(mb);

        if (user.getTipo().equals("ADMIN")) {
            getContentPane().add(new AdminPanel(), BorderLayout.CENTER);
        } else {
            getContentPane().add(new ResourcePanel(user), BorderLayout.CENTER);
        }
    }
}
