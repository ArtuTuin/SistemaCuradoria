package gui;

import javax.swing.*;
import java.awt.*;
import dao.ResourceDAO;
import model.User;
import model.Resource;
import java.util.List;

public class ResourcePanel extends JPanel {

    private User user;
    private JTable table;

    public ResourcePanel(User u) {
        this.user = u;

        setLayout(new BorderLayout(10,10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Cadastrar");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Excluir");
        JButton refresh = new JButton("Atualizar");

        top.add(add);
        top.add(edit);
        top.add(del);
        top.add(refresh);

        add(top, BorderLayout.NORTH);

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // AÇÕES
        add.addActionListener(e -> {
            ResourceFormDialog d = new ResourceFormDialog(user, null);
            d.setVisible(true);
            refreshTable();
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                int id = (int) table.getValueAt(r, 0);
                Resource res = ResourceDAO.findByUser(user.getId())
                    .stream().filter(x -> x.getId() == id).findFirst().orElse(null);

                if (res != null) {
                    ResourceFormDialog d = new ResourceFormDialog(user, res);
                    d.setVisible(true);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um recurso.");
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                int id = (int) table.getValueAt(r, 0);

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja excluir este recurso?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    ResourceDAO.delete(id);
                    refreshTable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecione um recurso.");
            }
        });

        refresh.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        List<Resource> recursos = ResourceDAO.findByUser(user.getId());
        String[] cols = {"ID","Título","Autor","Categoria"};

        Object[][] data = new Object[recursos.size()][4];

        for (int i = 0; i < recursos.size(); i++) {
            Resource r = recursos.get(i);
            data[i][0] = r.getId();
            data[i][1] = r.getTitulo();
            data[i][2] = r.getAutor();
            data[i][3] = r.getCategoria();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }
}
