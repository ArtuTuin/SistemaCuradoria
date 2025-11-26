package com.curadoria.view;

import com.curadoria.dao.UserDAO;
import com.curadoria.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {

    private final UserDAO dao = new UserDAO();
    private JTable tabela;
    private DefaultTableModel modelo;

    private JTextField nomeField;
    private JTextField idadeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JComboBox<String> tipoBox;
    private JTextField searchField;

    // 🌞 PALETA TEMA CLARO
    private final Color BG = new Color(0xF5F5F5);         // fundo principal
    private final Color PANEL = new Color(0xFFFFFF);      // cartões brancos
    private final Color PANEL_BORDER = new Color(0xDDDDDD);
    private final Color TEXT = new Color(0x222222);       // texto preto suave
    private final Color ACCENT = new Color(0x0096FF);     // azul-ciãmes
    private final Color ACCENT_HOVER = new Color(0x33ADFF);

    public AdminPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);

        add(criarHeader(), BorderLayout.NORTH);
        add(criarForm(), BorderLayout.CENTER);
        add(criarTabela(), BorderLayout.SOUTH);

        carregarUsuarios();
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Painel Administrativo – Gestão de Usuários");
        titulo.setForeground(TEXT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchArea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchArea.setBackground(PANEL);

        searchField = new JTextField(18);
        estilizarCampo(searchField);

        JButton btnBuscar = criarBotao("Buscar");
        JButton btnAtualizar = criarBotao("Atualizar");

        btnBuscar.addActionListener(e -> buscar());
        btnAtualizar.addActionListener(e -> carregarUsuarios());

        searchArea.add(searchField);
        searchArea.add(btnBuscar);
        searchArea.add(btnAtualizar);

        header.add(titulo, BorderLayout.WEST);
        header.add(searchArea, BorderLayout.EAST);

        return header;
    }

    private JPanel criarForm() {
        JPanel form = new JPanel();
        form.setBackground(PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        form.setLayout(new GridLayout(3, 4, 20, 15));

        nomeField = new JTextField();
        idadeField = new JTextField();
        emailField = new JTextField();
        senhaField = new JPasswordField();

        tipoBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        tipoBox.setBackground(Color.WHITE);
        tipoBox.setForeground(Color.BLACK);
        tipoBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        estilizarCampo(nomeField);
        estilizarCampo(idadeField);
        estilizarCampo(emailField);
        estilizarCampo(senhaField);

        form.add(criarLabel("Nome:"));  form.add(nomeField);
        form.add(criarLabel("Idade:")); form.add(idadeField);
        form.add(criarLabel("Email:")); form.add(emailField);
        form.add(criarLabel("Senha:")); form.add(senhaField);
        form.add(criarLabel("Tipo:"));  form.add(tipoBox);

        JButton btnCadastrar = criarBotao("Cadastrar Usuário");
        JButton btnEditar = criarBotao("Editar Selecionado");
        JButton btnExcluir = criarBotao("Excluir Selecionado");
        JButton btnToggle = criarBotao("Ativar / Desativar");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnToggle.addActionListener(e -> ativarDesativar());

        form.add(btnCadastrar);
        form.add(btnEditar);
        form.add(btnExcluir);
        form.add(btnToggle);

        return form;
    }

    private JLabel criarLabel(String texto){
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private JScrollPane criarTabela() {
        modelo = new DefaultTableModel(){
            @Override public boolean isCellEditable(int row, int col){ return false; }
            @Override public Class<?> getColumnClass(int col){
                return (col == 4) ? Boolean.class : String.class;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Email");
        modelo.addColumn("Tipo");
        modelo.addColumn("Ativo");

        tabela = new JTable(modelo);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(Color.BLACK);
        tabela.setRowHeight(28);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setGridColor(new Color(0xCCCCCC));

        JTableHeaderStyled(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(900, 280));
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    private void JTableHeaderStyled(JTable table){
        table.getTableHeader().setBackground(ACCENT);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);
    }

    private JButton criarBotao(String texto){
        JButton btn = new JButton(texto);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){
                btn.setBackground(ACCENT_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e){
                btn.setBackground(ACCENT);
            }
        });

        return btn;
    }

    private void estilizarCampo(JTextField field){
        field.setBackground(Color.WHITE);  
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_BORDER),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    // FUNÇÕES
    private void cadastrar(){
        try {
            User u = new User();
            u.setNome(nomeField.getText());
            u.setIdade(Integer.parseInt(idadeField.getText()));
            u.setEmail(emailField.getText());
            u.setTipo(tipoBox.getSelectedItem().toString());

            String senha = new String(senhaField.getPassword());
            if(senha.isBlank()){
                JOptionPane.showMessageDialog(this, "Senha inválida.");
                return;
            }

            if(dao.create(u, senha)){
                JOptionPane.showMessageDialog(this, "Usuário cadastrado!");
                carregarUsuarios();
                limparCampos();
            }

        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
        }
    }

    private void editar(){
        int row = tabela.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Selecione um usuário."); return;}

        int id = Integer.parseInt(modelo.getValueAt(row,0).toString());

        try {
            User u = new User();
            u.setId(id);
            u.setNome(nomeField.getText());
            u.setIdade(Integer.parseInt(idadeField.getText()));
            u.setTipo(tipoBox.getSelectedItem().toString());
            u.setAtivo((Boolean) modelo.getValueAt(row,4));

            if(dao.update(u)){
                JOptionPane.showMessageDialog(this,"Usuário atualizado!");
                carregarUsuarios();
            }

        } catch (Exception e){
            JOptionPane.showMessageDialog(this,"Erro ao editar.");
        }
    }

    private void excluir(){
        int row = tabela.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Selecione um usuário."); return;}

        int id = Integer.parseInt(modelo.getValueAt(row,0).toString());

        int opt = JOptionPane.showConfirmDialog(this, "Excluir usuário?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(opt == JOptionPane.YES_OPTION){
            dao.delete(id);
            carregarUsuarios();
        }
    }

    private void ativarDesativar(){
        int row = tabela.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Selecione um usuário."); return;}

        int id = Integer.parseInt(modelo.getValueAt(row,0).toString());
        boolean ativo = (Boolean) modelo.getValueAt(row,4);

        if(ativo){
            dao.deactivate(id);
        } else {
            dao.activate(id);
        }

        carregarUsuarios();
    }

    private void buscar(){
        modelo.setRowCount(0);
        List<User> lista = dao.search(searchField.getText());

        for(User u : lista){
            modelo.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getEmail(), u.getTipo(), u.isAtivo()
            });
        }
    }

    private void carregarUsuarios(){
        modelo.setRowCount(0);
        List<User> lista = dao.findAll();

        for(User u : lista){
            modelo.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getEmail(), u.getTipo(), u.isAtivo()
            });
        }
    }

    private void limparCampos(){
        nomeField.setText("");
        idadeField.setText("");
        emailField.setText("");
        senhaField.setText("");
    }
}
