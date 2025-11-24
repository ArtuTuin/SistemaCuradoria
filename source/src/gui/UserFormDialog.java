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
        setSize(350,350);
        setLocationRelativeTo(null);
        editing = u;
        init();
    }
    private void init() {
        JPanel p = new JPanel(new GridLayout(8,2,5,5));
        p.add(new JLabel("Nome:")); nomeF = new JTextField(); p.add(nomeF);
        p.add(new JLabel("Idade:")); idadeF = new JTextField(); p.add(idadeF);
        p.add(new JLabel("Email:")); emailF = new JTextField(); p.add(emailF);
        p.add(new JLabel("Tipo:")); tipoC = new JComboBox<>(new String[]{"ADMIN","COMUM"}); p.add(tipoC);
        p.add(new JLabel("Interesses (até 2):")); p.add(new JLabel(""));
        iaBox = new JCheckBox("IA Responsável"); ciberBox = new JCheckBox("Cibersegurança"); privBox = new JCheckBox("Privacidade");
        p.add(iaBox); p.add(ciberBox); p.add(privBox);
        p.add(new JLabel("Ativo:")); ativoBox = new JCheckBox(); ativoBox.setSelected(true); p.add(ativoBox);
        JButton save = new JButton("Salvar"); p.add(save);
        JButton cancel = new JButton("Cancelar"); p.add(cancel);
        add(p);

        if (editing!=null) {
            nomeF.setText(editing.getNome());
            idadeF.setText(String.valueOf(editing.getIdade()));
            emailF.setText(editing.getEmail());
            tipoC.setSelectedItem(editing.getTipo());
            ativoBox.setSelected(editing.isAtivo());
        }

        save.addActionListener(e -> {
            String nome = nomeF.getText().trim();
            int idade = Integer.parseInt(idadeF.getText().trim());
            String email = emailF.getText().trim();
            String tipo = (String) tipoC.getSelectedItem();
            boolean ativo = ativoBox.isSelected();
            User u = new User();
            if (editing!=null) u.setId(editing.getId());
            u.setNome(nome); u.setIdade(idade); u.setEmail(email); u.setTipo(tipo); u.setAtivo(ativo);
            // Password default for new users: usuario123
            if (editing==null) u.setSenhaPlain("usuario123");
            UserDAO.saveOrUpdate(u,
                iaBox.isSelected()?"IA":null,
                ciberBox.isSelected()?"CIBER":null,
                privBox.isSelected()?"PRIVACIDADE":null
            );
            dispose();
        });
        cancel.addActionListener(e -> dispose());
    }
}
