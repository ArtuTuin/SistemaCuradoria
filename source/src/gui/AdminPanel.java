package gui;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class AdminPanel extends JPanel {

    private JTable table;

    public AdminPanel() {

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton add = new JButton("Cadastrar Usuário");
        JButton edit = new JButton("Editar Selecionado");
        JButton refresh = new JButton("Atualizar");
        JButton delete = new JButton("Excluir Selecionado");
        JButton produtoButton = new JButton("Cadastrar Produto");
        JButton produtosBtn = new JButton("Produtos");

        // ordem dos botões
        top.add(produtoButton);
        top.add(produtosBtn);
        top.add(delete);
        top.add(add);
        top.add(edit);
        top.add(refresh);

        add(top, BorderLayout.NORTH);

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // AÇÃO DO BOTÃO CADASTRAR PRODUTO
        produtoButton.addActionListener(e -> {
            ProductFormDialog dialog = new ProductFormDialog(null); 
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        // AÇÃO DO BOTÃO GERENCIAR PRODUTOS
        produtosBtn.addActionListener(e -> {
            JFrame f = new JFrame("Gerenciar Produtos");
            f.setSize(600, 400);
            f.add(new ProductPanel());
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });

        // CADASTRAR USUÁRIO
        add.addActionListener(e -> {
            UserFormDialog d = new UserFormDialog(null);
            d.setVisible(true);
            refreshTable();
        });

        // EDITAR USUÁRIO
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                int id = (int) table.getValueAt(r, 0);
                User u = UserDAO.findById(id);
                if (u != null) {
                    UserFormDialog d = new UserFormDialog(u);
                    d.setVisible(true);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            }
        });

        // ATUALIZAR
        refresh.addActionListener(e -> refreshTable());

        // EXCLUIR
        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                int id = (int) table.getValueAt(r, 0);

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja excluir este usuário?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    UserDAO.delete(id);
                    refreshTable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            }
        });
    }

    private void refreshTable() {
        java.util.List<User> users = UserDAO.findAll();
        String[] cols = {"ID", "Nome", "Email", "Tipo", "Ativo"};

        Object[][] data = new Object[users.size()][5];

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i][0] = u.getId();
            data[i][1] = u.getNome();
            data[i][2] = u.getEmail();
            data[i][3] = u.getTipo();
            data[i][4] = u.isAtivo();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }
}
