package dao;

import db.DB;
import model.InteresseUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteresseUsuarioDAO {

    public static List<InteresseUsuario> findByUsuario(int userId) {
        List<InteresseUsuario> list = new ArrayList<>();
        try (Connection c = DB.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                "SELECT id, usuario_id, categoria FROM interesse_usuario WHERE usuario_id=?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InteresseUsuario iu = new InteresseUsuario();
                iu.setId(rs.getInt("id"));
                iu.setUsuarioId(rs.getInt("usuario_id"));
                iu.setCategoria(rs.getString("categoria"));
                list.add(iu);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }


    public static void insert(int userId, String categoria) {
        try (Connection c = DB.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO interesse_usuario (usuario_id, categoria) VALUES (?,?)"
            );

            ps.setInt(1, userId);
            ps.setString(2, categoria);
            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }


    public static void deleteByUsuario(int userId) {
        try (Connection c = DB.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                "DELETE FROM interesse_usuario WHERE usuario_id=?"
            );

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
