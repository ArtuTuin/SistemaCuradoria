package com.curadoria.dao;

import com.curadoria.config.DBConnection;
import com.curadoria.model.Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    // Criar um recurso
    public boolean create(Resource r){
        String sql = "INSERT INTO recursos (titulo, autor, categoria, pensamento, usuario_id) VALUES (?,?,?,?,?)";

        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getAutor());
            ps.setString(3, r.getCategoria());
            ps.setString(4, r.getPensamento());
            ps.setInt(5, r.getUsuarioId());

            return ps.executeUpdate() > 0;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Buscar todos os recursos do usuário ordenados por título
    public List<Resource> findByUserOrderByTitle(int userId){
        List<Resource> lista = new ArrayList<>();

        String sql = "SELECT * FROM recursos WHERE usuario_id = ? ORDER BY titulo ASC";

        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Resource r = new Resource();
                r.setId(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setAutor(rs.getString("autor"));
                r.setCategoria(rs.getString("categoria"));
                r.setPensamento(rs.getString("pensamento"));
                r.setUsuarioId(rs.getInt("usuario_id"));

                lista.add(r);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return lista;
    }
}
