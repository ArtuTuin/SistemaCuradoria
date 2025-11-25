package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;
import model.User;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    public LoginFrame() {
        setTitle("Curadoria - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,220);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }
    private void init() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(3,2,5,5));
        form.add(new JLabel("Email:"));
        emailField = new JTextField();
        form.add(emailField);
        form.add(new JLabel("Senha:"));
        passwordField = new JPasswordField();
        form.add(passwordField);
        JButton loginBtn = new JButton("Entrar");
        form.add(loginBtn);
        JButton exitBtn = new JButton("Sair");
        form.add(exitBtn);
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(form, BorderLayout.CENTER);
        add(p);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword());
            User user = UserDAO.login(email, pass);
            if (user != null && user.isAtivo()) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + user.getNome());
                new MainFrame(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login falhou.");
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));
    }
}
