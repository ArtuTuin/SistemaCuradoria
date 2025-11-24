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
        JButton add = new JButton("Cadastrar Recurso");
        JButton refresh = new JButton("Atualizar");
        top.add(add); top.add(refresh);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        add.addActionListener(e -> {
            ResourceFormDialog d = new ResourceFormDialog(user, null);
            d.setVisible(true);
            refreshTable();
        });
        refresh.addActionListener(e -> refreshTable());
    }
    private void refreshTable() {
        List<Resource> recursos = ResourceDAO.findByUser(user.getId());
        recursos.sort((a,b)->a.getTitulo().compareToIgnoreCase(b.getTitulo()));
        String[] cols = {"ID","Título","Autor","Categoria"};
        Object[][] data = new Object[recursos.size()][4];
        for (int i=0;i<recursos.size();i++) {
            Resource r = recursos.get(i);
            data[i][0]=r.getId();
            data[i][1]=r.getTitulo();
            data[i][2]=r.getAutor();
            data[i][3]=r.getCategoria();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }
}
