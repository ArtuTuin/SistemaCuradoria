package gui;

import javax.swing.*;
import java.awt.*;
import dao.ResourceDAO;
import model.Resource;
import model.User;

public class ResourceFormDialog extends JDialog {
    private JTextField tituloF, autorF;
    private JComboBox<String> categoriaC;

    private User user;
    private Resource editing;

    public ResourceFormDialog(User user, Resource r) {
        this.user = user;
        this.editing = r;

        setModal(true);
        setSize(350,250);
        setLocationRelativeTo(null);
        setTitle(editing == null ? "Cadastrar Recurso" : "Editar Recurso");

        init();
    }

    private void init() {
        JPanel p = new JPanel(new GridLayout(5,2,5,5));

        p.add(new JLabel("Título:"));
        tituloF = new JTextField();
        p.add(tituloF);

        p.add(new JLabel("Autor:"));
        autorF = new JTextField();
        p.add(autorF);

        p.add(new JLabel("Categoria:"));
        categoriaC = new JComboBox<>(new String[]{"IA", "CIBER", "PRIVACIDADE"});
        p.add(categoriaC);

        JButton save = new JButton("Salvar");
        JButton cancel = new JButton("Cancelar");

        p.add(save);
        p.add(cancel);

        add(p);

        // Se estiver editando, carregar dados
        if (editing != null) {
            tituloF.setText(editing.getTitulo());
            autorF.setText(editing.getAutor());
            categoriaC.setSelectedItem(editing.getCategoria());
        }

        save.addActionListener(e -> {
            String titulo = tituloF.getText().trim();
            String autor = autorF.getText().trim();
            String categoria = (String) categoriaC.getSelectedItem();

            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O título é obrigatório.");
                return;
            }

            Resource r = new Resource();
            if (editing != null) r.setId(editing.getId());
            r.setTitulo(titulo);
            r.setAutor(autor);
            r.setCategoria(categoria);
            r.setUsuarioId(user.getId());

            ResourceDAO.saveOrUpdate(r);

            dispose();
        });

        cancel.addActionListener(e -> dispose());
    }
}
