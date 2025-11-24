package gui;
import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;
import java.util.List;

public class AdminPanel extends JPanel {
    private JTable table;
    public AdminPanel() {
        setLayout(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Cadastrar Usuário");
        JButton edit = new JButton("Editar Selecionado");
        JButton refresh = new JButton("Atualizar");
        top.add(add); top.add(edit); top.add(refresh);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        add.addActionListener(e -> {
            UserFormDialog d = new UserFormDialog(null);
            d.setVisible(true);
            refreshTable();
        });
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r>=0) {
                int id = (int) table.getValueAt(r,0);
                User u = UserDAO.findById(id);
                if (u!=null) {
                    UserFormDialog d = new UserFormDialog(u);
                    d.setVisible(true);
                    refreshTable();
                }
            } else JOptionPane.showMessageDialog(this, "Selecione um usuário.");
        });
        refresh.addActionListener(e -> refreshTable());
    }
    private void refreshTable() {
        java.util.List<User> users = UserDAO.findAll();
        String[] cols = {"ID","Nome","Email","Tipo","Ativo"};
        Object[][] data = new Object[users.size()][5];
        for (int i=0;i<users.size();i++) {
            User u = users.get(i);
            data[i][0]=u.getId();
            data[i][1]=u.getNome();
            data[i][2]=u.getEmail();
            data[i][3]=u.getTipo();
            data[i][4]=u.isAtivo();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }
}
