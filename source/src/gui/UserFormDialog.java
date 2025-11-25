package gui;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class UserFormDialog extends JDialog {

    private JTextField nomeF, idadeF, emailF;
    private JComboBox<String> tipoC;
    private JCheckBox iaBox, ciberBox, privBox;
    private JCheckBox ativoBox;
    private User editing;

    public UserFormDialog(User u) {
        setModal(true);
        setSize(420, 380);
        setLocationRelativeTo(null);
        editing = u;
        setTitle(editing == null ? "Cadastrar Usuário" : "Editar Usuário");
        init();
    }

    private void init() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel do formulário
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
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

        // Interesses título
        c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel intTitle = new JLabel("Interesses (até 2):");
        intTitle.setFont(intTitle.getFont().deriveFont(Font.BOLD));
        form.add(intTitle, c);
        y++;

        // Interesses checkboxes
        JPanel interestsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        iaBox = new JCheckBox("IA Responsável");
        ciberBox = new JCheckBox("Cibersegurança");
        privBox = new JCheckBox("Privacidade");
        interestsPanel.add(iaBox);
        interestsPanel.add(ciberBox);
        interestsPanel.add(privBox);

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

        // Preenche campos se estiver editando
        if (editing != null) {
            nomeF.setText(editing.getNome());
            idadeF.setText(String.valueOf(editing.getIdade()));
            emailF.setText(editing.getEmail());
            tipoC.setSelectedItem(editing.getTipo());
            ativoBox.setSelected(editing.isAtivo());
        }

        // Ação salvar
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

            // Senha padrão para novos usuários
            if (editing == null)
                u.setSenhaPlain("usuario123");

            UserDAO.saveOrUpdate(
                u,
                iaBox.isSelected() ? "IA" : null,
                ciberBox.isSelected() ? "CIBER" : null,
                privBox.isSelected() ? "PRIVACIDADE" : null
            );

            dispose();
        });

        cancel.addActionListener(e -> dispose());
    }
}
