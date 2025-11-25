package gui;

import javax.swing.*;
import java.awt.*;
import dao.ProductDAO;
import model.Product;

public class ProductFormDialog extends JDialog {

    private JTextField nomeField;
    private JTextField precoField;
    private JTextField estoqueField;
    private JButton salvarBtn;

    private Product produto; // se for edição, NÃO é null

    // construtor sem-arg para compatibilidade: new ProductFormDialog()
    public ProductFormDialog() {
        this(null);
    }

    public ProductFormDialog(Product p) {
        this.produto = p;

        setTitle(p == null ? "Cadastrar Produto" : "Editar Produto");
        setSize(350, 250);
        setModal(true);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField(20);

        JLabel precoLabel = new JLabel("Preço:");
        precoField = new JTextField(20);

        JLabel estoqueLabel = new JLabel("Estoque:");
        estoqueField = new JTextField(20);

        salvarBtn = new JButton(p == null ? "Cadastrar" : "Salvar Alterações");

        // Layout
        c.gridx = 0; c.gridy = 0;
        add(nomeLabel, c);
        c.gridx = 1;
        add(nomeField, c);

        c.gridx = 0; c.gridy = 1;
        add(precoLabel, c);
        c.gridx = 1;
        add(precoField, c);

        c.gridx = 0; c.gridy = 2;
        add(estoqueLabel, c);
        c.gridx = 1;
        add(estoqueField, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        add(salvarBtn, c);

        // preencher campos em modo edição
        if (produto != null) {
            nomeField.setText(produto.getNome());
            precoField.setText(String.valueOf(produto.getPreco()));
            estoqueField.setText(String.valueOf(produto.getEstoque()));
        }

        salvarBtn.addActionListener(e -> salvarProduto());
    }

    private void salvarProduto() {
        try {
            String nome = nomeField.getText().trim();
            double preco = Double.parseDouble(precoField.getText().trim());
            int estoque = Integer.parseInt(estoqueField.getText().trim());

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.");
                return;
            }

            if (produto == null) {
                // usa insert (seu DAO tem insert)
                Product novo = new Product();
                novo.setNome(nome);
                novo.setPreco(preco);
                novo.setEstoque(estoque);

                ProductDAO.insert(novo);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            } else {
                // usa update
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setEstoque(estoque);

                ProductDAO.update(produto);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e Estoque devem ser valores numéricos.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage());
        }
    }
}
