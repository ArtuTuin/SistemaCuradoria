package com.curadoria.view;

import com.curadoria.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal para usuário comum (tema claro).
 * Usa BorderFactory.createEmptyBorder(...) para evitar problemas de import.
 */
public class UserMainFrame extends JFrame {

    // Cores do tema claro (mesmas do AdminPanel / LoginFrame)
    private final Color BG = new Color(0xF5F5F5);
    private final Color CARD = new Color(0xFFFFFF);
    private final Color BORDER = new Color(0xDDDDDD);
    private final Color TEXT = new Color(0x222222);
    private final Color ACCENT = new Color(0x0096FF);
    private final Color ACCENT_HOVER = new Color(0x33ADFF);

    private final User user;

    public UserMainFrame(User user) {
        this.user = user;
        setTitle("Curadoria — " + user.getNome());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        initUI();
    }

    private void initUI() {
        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(CARD);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        top.setPreferredSize(new Dimension(getWidth(), 64));

        JLabel title = new JLabel("Curadoria — Meus Materiais");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rightControls.setBackground(CARD);

        JLabel userLabel = new JLabel("Olá, " + user.getNome());
        userLabel.setForeground(TEXT);

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(ACCENT);
        btnSair.setForeground(Color.DARK_GRAY);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> {
            // Volta ao login
            new LoginFrame().setVisible(true);
            dispose();
        });

        rightControls.add(userLabel);
        rightControls.add(btnSair);

        top.add(title, BorderLayout.WEST);
        top.add(rightControls, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Left menu (simples)
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(200, getHeight()));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(CARD);
        left.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        JButton btnRecursos = new JButton("Recursos");
        btnRecursos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecursos.setMaximumSize(new Dimension(180, 40));
        styleMenuButton(btnRecursos);

        JButton btnPerfil = new JButton("Meu Perfil");
        btnPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPerfil.setMaximumSize(new Dimension(180, 40));
        styleMenuButton(btnPerfil);

        left.add(Box.createVerticalStrut(30));
        left.add(btnRecursos);
        left.add(Box.createVerticalStrut(8));
        left.add(btnPerfil);

        add(left, BorderLayout.WEST);

        // Center panel: usa o UserPanel (que contém formulários e tabela)
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(BG);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Tenta instanciar UserPanel
        try {
            UserPanel userPanel = new UserPanel(user);
            centerWrapper.add(userPanel, BorderLayout.CENTER);
        } catch (Throwable t) {
            // Se houver problema ao instanciar UserPanel, mostra uma mensagem amigável
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(CARD);
            JLabel err = new JLabel("<html><div style='padding:20px;'>Erro ao carregar painel do usuário.<br>"
                    + "Verifique se a classe <b>com.curadoria.view.UserPanel</b> existe e está compilada.<br>"
                    + "Erro: " + t.getClass().getSimpleName() + " - " + t.getMessage() + "</div></html>");
            err.setForeground(TEXT);
            errorPanel.add(err, BorderLayout.CENTER);
            centerWrapper.add(errorPanel, BorderLayout.CENTER);
            t.printStackTrace();
        }

        add(centerWrapper, BorderLayout.CENTER);

        // Botões do menu acionam troca de conteúdo (somente recursos implementado aqui)
        btnRecursos.addActionListener(e -> {
            centerWrapper.removeAll();
            centerWrapper.add(new UserPanel(user), BorderLayout.CENTER);
            centerWrapper.revalidate();
            centerWrapper.repaint();
        });

        btnPerfil.addActionListener(e -> {
            // Exemplo simples de painel de perfil
            JPanel perfil = new JPanel();
            perfil.setBackground(Color.WHITE);
            perfil.setLayout(new BoxLayout(perfil, BoxLayout.Y_AXIS));
            perfil.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

            JLabel nome = new JLabel("Nome: " + user.getNome());
            nome.setForeground(TEXT);
            perfil.add(nome);
            perfil.add(Box.createVerticalStrut(8));
            JLabel email = new JLabel("Email: " + user.getEmail());
            email.setForeground(TEXT);
            perfil.add(email);

            centerWrapper.removeAll();
            centerWrapper.add(perfil, BorderLayout.NORTH);
            centerWrapper.revalidate();
            centerWrapper.repaint();
        });
    }

    private void styleMenuButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0x222222));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
    }
}
