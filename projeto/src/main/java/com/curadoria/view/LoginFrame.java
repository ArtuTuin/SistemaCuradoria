package com.curadoria.view;

import com.curadoria.dao.UserDAO;
import com.curadoria.model.User;
import com.curadoria.utils.HashUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;

    // 🌞 Paleta clara
    private final Color BG = new Color(0xF5F5F5);
    private final Color PANEL = new Color(0xFFFFFF);
    private final Color PANEL_BORDER = new Color(0xDDDDDD);
    private final Color TEXT = new Color(0x444444);
    private final Color ACCENT = new Color(0x0096FF);
    private final Color ACCENT_HOVER = new Color(0x33ADFF);

    public LoginFrame() {
        setTitle("Login - Sistema de Curadoria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        add(main);

        // Painel branco central
        JPanel card = new JPanel();
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_BORDER),
                new EmptyBorder(30, 30, 30, 30)
        ));
        card.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel titulo = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(TEXT);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(TEXT);
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        emailField = new JTextField();
        estilizarCampo(emailField);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setForeground(TEXT);
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        senhaField = new JPasswordField();
        estilizarCampo(senhaField);

        JButton btnEntrar = criarBotao("Entrar");
        btnEntrar.addActionListener(e -> login());

        card.add(titulo);
        card.add(lblEmail);
        card.add(emailField);
        card.add(lblSenha);
        card.add(senhaField);
        card.add(btnEntrar);

        main.add(card, BorderLayout.CENTER);
    }


    // 🌞 Estilo dos campos
    private void estilizarCampo(JTextField field){
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    // 🌞 Botão azul-ciãmes com hover
    private JButton criarBotao(String texto){
        JButton btn = new JButton(texto);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.DARK_GRAY); // só muda a letra
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){
                btn.setBackground(ACCENT_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e){
                btn.setBackground(ACCENT);
            }
        });

        return btn;
    }

    // 🔐 Lógica de login (mantida)
    private void login(){
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());

        if(email.isBlank() || senha.isBlank()){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        UserDAO dao = new UserDAO();
        User u = dao.findByEmail(email);

        if(u == null){
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            return;
        }

        String hash = HashUtil.sha256(senha);

        if(!hash.equals(u.getSenhaHash())){
            JOptionPane.showMessageDialog(this, "Senha incorreta.");
            return;
        }

        // Sucesso → abre painel
        if(u.getTipo().equalsIgnoreCase("ADMIN")){
            new MainFrame(u).setVisible(true);
        } else {
            new UserMainFrame(u).setVisible(true);
        }

        dispose();
    }

}
