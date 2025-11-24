package gui;
import javax.swing.*;
import java.awt.*;
import dao.ResourceDAO;
import model.User;
import model.Resource;

public class ResourceFormDialog extends JDialog {
    private JTextField tituloF, autorF;
    private JComboBox<String> catC;
    private User user;
    public ResourceFormDialog(User u, Resource r) {
        setModal(true);
        setSize(350,220);
        setLocationRelativeTo(null);
        this.user = u;
        init();
    }
    private void init() {
        JPanel p = new JPanel(new GridLayout(4,2,5,5));
        p.add(new JLabel("Título:")); tituloF = new JTextField(); p.add(tituloF);
        p.add(new JLabel("Autor:")); autorF = new JTextField(); p.add(autorF);
        p.add(new JLabel("Categoria:")); catC = new JComboBox<>(new String[]{"IA","CIBER","PRIVACIDADE"}); p.add(catC);
        JButton save = new JButton("Salvar"); p.add(save);
        JButton cancel = new JButton("Cancelar"); p.add(cancel);
        add(p);

        save.addActionListener(e -> {
            String titulo = tituloF.getText().trim();
            String autor = autorF.getText().trim();
            String categoria = (String) catC.getSelectedItem();
            Resource rec = new Resource();
            rec.setTitulo(titulo); rec.setAutor(autor); rec.setCategoria(categoria); rec.setUsuarioId(user.getId());
            ResourceDAO.save(rec);
            dispose();
        });
        cancel.addActionListener(e -> dispose());
    }
}
