package com.curadoria.dao;

import com.curadoria.config.DBConnection;
import com.curadoria.model.User;
import com.curadoria.utils.HashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByEmail(String email){
        try (Connection c = DBConnection.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setIdade(rs.getInt("idade"));
                u.setEmail(rs.getString("email"));
                u.setSenhaHash(rs.getString("senha_hash"));
                u.setTipo(rs.getString("tipo"));
                u.setAtivo(rs.getBoolean("ativo"));
                return u;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll(){
        List<User> lista = new ArrayList<>();

        try (Connection c = DBConnection.getConnection()) {
            String sql = "SELECT * FROM usuarios ORDER BY nome ASC";
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                lista.add(extractUser(rs));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public List<User> search(String termo){
        List<User> lista = new ArrayList<>();

        try(Connection c = DBConnection.getConnection()){
            String sql = "SELECT * FROM usuarios WHERE nome LIKE ? OR email LIKE ? ORDER BY nome ASC";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + termo + "%");
            ps.setString(2, "%" + termo + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                lista.add(extractUser(rs));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public boolean create(User u, String senha) {
        try(Connection c = DBConnection.getConnection()){
            String sql = "INSERT INTO usuarios (nome, idade, email, senha_hash, tipo, ativo) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, u.getNome());
            ps.setInt(2, u.getIdade());
            ps.setString(3, u.getEmail());
            ps.setString(4, HashUtil.sha256(senha));
            ps.setString(5, u.getTipo());
            ps.setBoolean(6, true);

            return ps.executeUpdate() == 1;

        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(User u){
        try(Connection c = DBConnection.getConnection()){
            String sql = "UPDATE usuarios SET nome=?, idade=?, tipo=?, ativo=? WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, u.getNome());
            ps.setInt(2, u.getIdade());
            ps.setString(3, u.getTipo());
            ps.setBoolean(4, u.isAtivo());
            ps.setInt(5, u.getId());

            return ps.executeUpdate() == 1;

        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deactivate(int id){
        return setActive(id, false);
    }

    public boolean activate(int id){
        return setActive(id, true);
    }

    private boolean setActive(int id, boolean active){
        try(Connection c = DBConnection.getConnection()){
            String sql = "UPDATE usuarios SET ativo=? WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setBoolean(1, active);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;

        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        try(Connection c = DBConnection.getConnection()){
            String sql = "DELETE FROM usuarios WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private User extractUser(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setTipo(rs.getString("tipo"));
        u.setIdade(rs.getInt("idade"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setAtivo(rs.getBoolean("ativo"));
        return u;
    }
}
