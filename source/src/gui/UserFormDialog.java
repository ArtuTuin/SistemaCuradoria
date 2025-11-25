package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import dao.ProductDAO;
import model.User;
import model.Product;

public class UserFormDialog extends JDialog {

    private JTextField nomeF, idadeF, emailF;
    private JComboBox<String> tipoC;
    private JCheckBox iaBox, ciberBox, privBox;
    private JCheckBox ativoBox;

    // NOVO -> lista para armazenar checkboxes dos produtos
    private List<JCheckBox> produtosBoxes = new ArrayList<>();

    private User editing;

    public UserFormDialog(User u) {
        setModal(true);
        setSize(480, 520);
        setLocationRelativeTo(null);
        editing = u;
        setTitle(editing == null ? "Cadastrar Usuário" : "Editar Usuário");
        init();
    }

    private void init() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Nome
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Nome:"), c);
        nomeF = new JTextField();
        c.gridx = 1; form.add(nomeF, c);
        y++;

        // Idade
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Idade:"), c);
        idadeF = new JTextField();
        c.gridx = 1; form.add(idadeF, c);
        y++;

        // Email
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Email:"), c);
        emailF = new JTextField();
        c.gridx = 1; form.add(emailF, c);
        y++;

        // Tipo
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Tipo:"), c);
        tipoC = new JComboBox<>(new String[]{"ADMIN", "COMUM"});
        c.gridx = 1; form.add(tipoC, c);
        y++;

        // Título interesses
        c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel intTitle = new JLabel("Interesses :");
        intTitle.setFont(intTitle.getFont().deriveFont(Font.BOLD));
        form.add(intTitle, c);
        y++;

        // Painel de interesses
        JPanel interestsPanel = new JPanel();
        interestsPanel.setLayout(new GridLayout(0, 1, 5, 5));

        // Interesses fixos
        iaBox = new JCheckBox("IA Responsável");
        ciberBox = new JCheckBox("Cibersegurança");
        privBox = new JCheckBox("Privacidade");

        interestsPanel.add(iaBox);
        interestsPanel.add(ciberBox);
        interestsPanel.add(privBox);

        // ===== ADICIONAR PRODUTOS COMO CHECKBOX =====
        List<Product> produtos = ProductDAO.findAll();

        for (Product p : produtos) {
            JCheckBox cb = new JCheckBox( p.getNome());
            produtosBoxes.add(cb);
            interestsPanel.add(cb);
        }

        // Adiciona o painel no form
        c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        form.add(interestsPanel, c);
        y++;

        // Ativo
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Ativo:"), c);
        ativoBox = new JCheckBox();
        ativoBox.setSelected(true);
        c.gridx = 1; form.add(ativoBox, c);
        y++;

        // Botões
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Salvar");
        JButton cancel = new JButton("Cancelar");
        buttons.add(save);
        buttons.add(cancel);

        main.add(form, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        add(main);

        // Preencher se estiver editando
        if (editing != null) {
            nomeF.setText(editing.getNome());
            idadeF.setText(String.valueOf(editing.getIdade()));
            emailF.setText(editing.getEmail());
            tipoC.setSelectedItem(editing.getTipo());
            ativoBox.setSelected(editing.isAtivo());

            // Marcar os checkboxes fixos (SE você tiver isso no banco)
            // aqui depois posso integrar 100% para você se mandar o UserDAO
        }

        // ------ BOTÃO SALVAR ------
        save.addActionListener(e -> {

            String nome = nomeF.getText().trim();
            int idade = Integer.parseInt(idadeF.getText().trim());
            String email = emailF.getText().trim();
            String tipo = (String) tipoC.getSelectedItem();
            boolean ativo = ativoBox.isSelected();

            User u = new User();
            if (editing != null) u.setId(editing.getId());

            u.setNome(nome);
            u.setIdade(idade);
            u.setEmail(email);
            u.setTipo(tipo);
            u.setAtivo(ativo);

            // senha padrão
            if (editing == null)
                u.setSenhaPlain("usuario123");

            // interesses fixos
            List<String> interesses = new ArrayList<>();

            if (iaBox.isSelected()) interesses.add("IA");
            if (ciberBox.isSelected()) interesses.add("CIBER");
            if (privBox.isSelected()) interesses.add("PRIVACIDADE");

            // produtos marcados
            for (JCheckBox cb : produtosBoxes) {
                if (cb.isSelected()) {
                    interesses.add(cb.getText()); // "Produto: Nome"
                }
            }

            // Salvar tudo
            UserDAO.saveOrUpdate(u, interesses);

            dispose();
        });

        cancel.addActionListener(e -> dispose());
    }
}
