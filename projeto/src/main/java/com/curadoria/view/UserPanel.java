package com.curadoria.view;

import com.curadoria.dao.ResourceDAO;
import com.curadoria.model.Resource;
import com.curadoria.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserPanel extends JPanel {

    private User user;
    private ResourceDAO dao = new ResourceDAO();
    private DefaultTableModel model;

    private JTextField tituloField;
    private JTextField autorField;
    private JComboBox<String> categoriaBox;
    private JTextArea pensamentoArea;

    private JTextField searchField;
    private JComboBox<String> filtroCategoria;

    private JTable tabela;
    private List<Resource> listaCompleta;

    private final Color BG = new Color(0xF5F5F5);
    private final Color CARD = new Color(0xFFFFFF);
    private final Color BORDER = new Color(0xDDDDDD);
    private final Color TEXT = new Color(0x222222);
    private final Color ACCENT = new Color(0x0096FF);

    public UserPanel(User user){
        this.user = user;

        setLayout(new BorderLayout());
        setBackground(BG);

        // ====================
        // Título
        // ====================
        JLabel title = new JLabel("Meus Recursos", SwingConstants.CENTER);
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(title, BorderLayout.NORTH);


        // ====================
        // FORM
        // ====================

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(20,20,20,20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Título:"), gbc);

        tituloField = new JTextField();
        estilizarCampo(tituloField);
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(tituloField, gbc);

        // Autor
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Autor:"), gbc);

        autorField = new JTextField();
        estilizarCampo(autorField);
        gbc.gridx = 1; gbc.gridy = 1;
        form.add(autorField, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Categoria:"), gbc);

        categoriaBox = new JComboBox<>(new String[]{
                "IA Responsável",
                "Cibersegurança",
                "Privacidade & Ética Digital"
        });
        categoriaBox.setBackground(Color.WHITE);
        categoriaBox.setForeground(TEXT);
        gbc.gridx = 1; gbc.gridy = 2;
        form.add(categoriaBox, gbc);

        // Pensamento
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("O que você achou?"), gbc);

        pensamentoArea = new JTextArea(3, 20);
        pensamentoArea.setLineWrap(true);
        pensamentoArea.setWrapStyleWord(true);
        pensamentoArea.setBorder(BorderFactory.createLineBorder(BORDER));
        gbc.gridx = 1; gbc.gridy = 3;
        form.add(new JScrollPane(pensamentoArea), gbc);

        // Botões lado a lado
        JPanel botoes = new JPanel(new GridLayout(1,3,10,10));
        botoes.setBackground(CARD);

        JButton cadastrarBtn = new JButton("Salvar Recurso");
        cadastrarBtn.setBackground(ACCENT);
        cadastrarBtn.setForeground(Color.DARK_GRAY);
        cadastrarBtn.setFocusPainted(false);
        cadastrarBtn.addActionListener(e -> cadastrar());

        JButton editarBtn = new JButton("Editar Selecionado");
        editarBtn.setBackground(Color.GRAY);
        editarBtn.setForeground(Color.DARK_GRAY);
        editarBtn.setFocusPainted(false);
        editarBtn.addActionListener(e -> editarRecurso());

        JButton excluirBtn = new JButton("Excluir Selecionado");
        excluirBtn.setBackground(new Color(200,50,50));
        excluirBtn.setForeground(Color.DARK_GRAY);
        excluirBtn.setFocusPainted(false);
        excluirBtn.addActionListener(e -> excluirRecurso());

        botoes.add(cadastrarBtn);
        botoes.add(editarBtn);
        botoes.add(excluirBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(botoes, gbc);

        add(form, BorderLayout.CENTER);


        // ====================
        // FILTROS E BUSCA
        // ====================

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.setBackground(BG);

        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createLineBorder(BORDER));
        searchField.addCaretListener(e -> aplicarFiltros());

        filtroCategoria = new JComboBox<>(new String[]{
                "Todas",
                "IA Responsável",
                "Cibersegurança",
                "Privacidade & Ética Digital"
        });
        filtroCategoria.addActionListener(e -> aplicarFiltros());

        filtros.add(new JLabel("Buscar título:"));
        filtros.add(searchField);

        filtros.add(new JLabel("Categoria:"));
        filtros.add(filtroCategoria);

        add(filtros, BorderLayout.WEST);


        // ====================
        // TABELA
        // ====================
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Título");
        model.addColumn("Autor");
        model.addColumn("Categoria");
        model.addColumn("Pensamento");

        tabela = new JTable(model);
        tabela.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        add(scroll, BorderLayout.SOUTH);

        carregarLista();
    }

    private void estilizarCampo(JTextField field){
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createLineBorder(BORDER));
    }


    // ================================
    // CADASTRAR
    // ================================
    private void cadastrar(){
        Resource r = new Resource();
        r.setTitulo(tituloField.getText());
        r.setAutor(autorField.getText());
        r.setCategoria(categoriaBox.getSelectedItem().toString());
        r.setPensamento(pensamentoArea.getText());
        r.setUsuarioId(user.getId());

        if(dao.create(r)){
            JOptionPane.showMessageDialog(this, "Recurso adicionado!");
            limparCampos();
            carregarLista();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar.");
        }
    }

    // ================================
    // EDITAR
    // ================================
    private void editarRecurso(){
        int row = tabela.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this, "Selecione um recurso!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        Resource r = new Resource();
        r.setId(id);
        r.setTitulo(tituloField.getText());
        r.setAutor(autorField.getText());
        r.setCategoria(categoriaBox.getSelectedItem().toString());
        r.setPensamento(pensamentoArea.getText());
        r.setUsuarioId(user.getId());

        String sql = "UPDATE recursos SET titulo=?, autor=?, categoria=?, pensamento=? WHERE id=?";

        try (var c = com.curadoria.config.DBConnection.getConnection();
             var ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getAutor());
            ps.setString(3, r.getCategoria());
            ps.setString(4, r.getPensamento());
            ps.setInt(5, r.getId());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Recurso atualizado!");

            carregarLista();

        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao editar.");
        }
    }

    // ================================
    // EXCLUIR
    // ================================
    private void excluirRecurso(){
        int row = tabela.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this, "Selecione um recurso!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        int opc = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if(opc != JOptionPane.YES_OPTION)
            return;

        try (var c = com.curadoria.config.DBConnection.getConnection();
             var ps = c.prepareStatement("DELETE FROM recursos WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Recurso excluído!");
            carregarLista();

        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao excluir.");
        }
    }


    // ================================
    // LISTAR RECURSOS
    // ================================
    private void carregarLista(){
        model.setRowCount(0);
        listaCompleta = dao.findByUserOrderByTitle(user.getId());

        for(Resource r : listaCompleta){
            model.addRow(new Object[]{
                    r.getId(),
                    r.getTitulo(),
                    r.getAutor(),
                    r.getCategoria(),
                    r.getPensamento()
            });
        }
    }

    private void aplicarFiltros(){
        String termo = searchField.getText().toLowerCase();
        String categoria = filtroCategoria.getSelectedItem().toString();

        List<Resource> filtrados = listaCompleta.stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(termo))
                .filter(r -> categoria.equals("Todas") || r.getCategoria().equals(categoria))
                .collect(Collectors.toList());

        model.setRowCount(0);
        for(Resource r : filtrados){
            model.addRow(new Object[]{
                    r.getId(),
                    r.getTitulo(),
                    r.getAutor(),
                    r.getCategoria(),
                    r.getPensamento()
            });
        }
    }

    private void limparCampos(){
        tituloField.setText("");
        autorField.setText("");
        pensamentoArea.setText("");
    }
}
