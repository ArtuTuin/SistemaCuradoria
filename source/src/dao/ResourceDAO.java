package dao;

import db.DB;
import model.Resource;
import java.sql.*;
import java.util.*;

public class ResourceDAO {

    public static List<Resource> findByUser(int userId) {
        List<Resource> list = new ArrayList<>();

        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "SELECT id, usuario_id, titulo, autor, categoria FROM recurso WHERE usuario_id=?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource r = new Resource();
                r.setId(rs.getInt("id"));
                r.setUsuarioId(rs.getInt("usuario_id"));
                r.setTitulo(rs.getString("titulo"));
                r.setAutor(rs.getString("autor"));
                r.setCategoria(rs.getString("categoria"));
                list.add(r);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public static void saveOrUpdate(Resource r) {
        try (Connection c = DB.getConnection()) {

            if (r.getId() == 0) {
                // INSERT
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO recurso (usuario_id, titulo, autor, categoria) VALUES (?,?,?,?)"
                );
                ps.setInt(1, r.getUsuarioId());
                ps.setString(2, r.getTitulo());
                ps.setString(3, r.getAutor());
                ps.setString(4, r.getCategoria());
                ps.executeUpdate();

            } else {
                // UPDATE
                PreparedStatement ps = c.prepareStatement(
                    "UPDATE recurso SET titulo=?, autor=?, categoria=? WHERE id=?"
                );
                ps.setString(1, r.getTitulo());
                ps.setString(2, r.getAutor());
                ps.setString(3, r.getCategoria());
                ps.setInt(4, r.getId());
                ps.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void delete(int id) {
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM recurso WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
