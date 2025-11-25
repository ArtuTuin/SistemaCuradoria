package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProductDAO;
import model.Product;

public class ProductPanel extends JPanel {

    private JTable table;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));

        // ---------- TOPO (BOTÕES) ----------
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAdd = new JButton("Cadastrar Produto");
        JButton btnEdit = new JButton("Editar Produto");
        JButton btnDelete = new JButton("Excluir Produto");
        JButton btnRefresh = new JButton("Atualizar");

        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDelete);
        top.add(btnRefresh);

        add(top, BorderLayout.NORTH);

        // ---------- TABELA ----------
        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nome", "Preço", "Estoque"}
        ));

        add(new JScrollPane(table), BorderLayout.CENTER);

        carregarTabela();

        // ---------- AÇÕES DOS BOTÕES ----------

        // Cadastrar
        btnAdd.addActionListener(e -> {
            ProductFormDialog dialog = new ProductFormDialog(); // construtor sem-args está disponível
            dialog.setVisible(true);
            carregarTabela();
        });

        // Editar
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
                return;
            }

            int id = (int) table.getValueAt(row, 0);
            Product p = ProductDAO.findById(id); // usa findById (existente no seu DAO)

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
                return;
            }

            ProductFormDialog dialog = new ProductFormDialog(p); // modo edição
            dialog.setVisible(true);
            carregarTabela();
        });

        // Excluir
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
                return;
            }

            int id = (int) table.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Tem certeza que deseja excluir este produto?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                ProductDAO.delete(id); // usa delete (existente no seu DAO)
                carregarTabela();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            }
        });

        // Atualizar
        btnRefresh.addActionListener(e -> carregarTabela());
    }

    // ---------- MÉTODO QUE ATUALIZA A TABELA ----------
    private void carregarTabela() {
        List<Product> produtos = ProductDAO.findAll(); // usa findAll (existente no seu DAO)

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Product p : produtos) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getPreco(),
                p.getEstoque()
            });
        }
    }
}
