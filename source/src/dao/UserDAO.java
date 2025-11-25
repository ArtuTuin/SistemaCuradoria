package dao;

import model.User;
import db.DB;

import java.sql.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

public class UserDAO {

    // ---------- LOGIN ----------
    public static User login(String email, String password) {
        try (Connection c = DB.getConnection()) {
            String sql = "SELECT id,nome,idade,tipo,email,senha_hash,salt,ativo FROM usuario WHERE email=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("senha_hash");
                String salt = rs.getString("salt");
                String candidate = sha256(salt + password);

                if (hash.equals(candidate)) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setIdade(rs.getInt("idade"));
                    u.setTipo(rs.getString("tipo"));
                    u.setEmail(rs.getString("email"));
                    u.setAtivo(rs.getBoolean("ativo"));
                    return u;
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        return null;
    }

    // ---------- SELECT ALL ----------
    public static java.util.List<User> findAll() {
        java.util.List<User> list = new java.util.ArrayList<>();

        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "SELECT id,nome,idade,tipo,email,ativo FROM usuario"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setIdade(rs.getInt("idade"));
                u.setTipo(rs.getString("tipo"));
                u.setEmail(rs.getString("email"));
                u.setAtivo(rs.getBoolean("ativo"));

                list.add(u);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        return list;
    }

    // ---------- SELECT BY ID ----------
    public static User findById(int id) {
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "SELECT id,nome,idade,tipo,email,ativo FROM usuario WHERE id=?"
            );
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setIdade(rs.getInt("idade"));
                u.setTipo(rs.getString("tipo"));
                u.setEmail(rs.getString("email"));
                u.setAtivo(rs.getBoolean("ativo"));
                return u;
            }

        } catch (Exception ex) { ex.printStackTrace(); }

        return null;
    }

    // ---------- INSERT / UPDATE ----------
    public static void saveOrUpdate(User u, List<String> categorias) {
        try (Connection c = DB.getConnection()) {

            if (u.getId() == 0) {
                // ---------- INSERT ----------
                String salt = genSalt();
                String pwd = u.getSenhaPlain() != null ? u.getSenhaPlain() : "usuario123";
                String hash = sha256(salt + pwd);

                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO usuario (nome,idade,tipo,email,senha_hash,salt,ativo) VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, u.getNome());
                ps.setInt(2, u.getIdade());
                ps.setString(3, u.getTipo());
                ps.setString(4, u.getEmail());
                ps.setString(5, hash);
                ps.setString(6, salt);
                ps.setBoolean(7, u.isAtivo());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                int userId = 0;
                if (keys.next()) userId = keys.getInt(1);

                // Inserir interesses
                PreparedStatement pi = c.prepareStatement(
                    "INSERT INTO interesse_usuario (usuario_id,categoria) VALUES (?,?)"
                );

                for (String cat : categorias) {
                    pi.setInt(1, userId);
                    pi.setString(2, cat);
                    pi.executeUpdate();
                }

            } else {

                // ---------- UPDATE ----------
                PreparedStatement ps = c.prepareStatement(
                    "UPDATE usuario SET nome=?,idade=?,tipo=?,email=?,ativo=? WHERE id=?"
                );

                ps.setString(1, u.getNome());
                ps.setInt(2, u.getIdade());
                ps.setString(3, u.getTipo());
                ps.setString(4, u.getEmail());
                ps.setBoolean(5, u.isAtivo());
                ps.setInt(6, u.getId());
                ps.executeUpdate();

                // Apaga interesses
                PreparedStatement del = c.prepareStatement(
                    "DELETE FROM interesse_usuario WHERE usuario_id=?"
                );
                del.setInt(1, u.getId());
                del.executeUpdate();

                // Insere novamente
                PreparedStatement pi = c.prepareStatement(
                    "INSERT INTO interesse_usuario (usuario_id,categoria) VALUES (?,?)"
                );

                for (String cat : categorias) {
                    pi.setInt(1, u.getId());
                    pi.setString(2, cat);
                    pi.executeUpdate();
                }
            }

        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // ---------- DELETE ----------
    public static void delete(int id) {
        try (Connection c = DB.getConnection()) {

            PreparedStatement delInt = c.prepareStatement(
                "DELETE FROM interesse_usuario WHERE usuario_id=?"
            );
            delInt.setInt(1, id);
            delInt.executeUpdate();

            PreparedStatement ps = c.prepareStatement(
                "DELETE FROM usuario WHERE id=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception ex) { ex.printStackTrace(); }
    }


    // ---------- UTILS ----------
    private static String sha256(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] b = md.digest(s.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }

    private static String genSalt() throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte x : salt) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}
