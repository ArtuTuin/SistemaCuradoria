package dao;
import model.User;
import db.DB;
import java.sql.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class UserDAO {
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
        } catch(Exception ex){ ex.printStackTrace(); }
        return null;
    }
    public static List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT id,nome,idade,tipo,email,ativo FROM usuario");
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
        } catch(Exception ex){ ex.printStackTrace(); }
        return list;
    }
    public static User findById(int id) {
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT id,nome,idade,tipo,email,ativo FROM usuario WHERE id=?");
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
        } catch(Exception ex){ ex.printStackTrace(); }
        return null;
    }
    public static void saveOrUpdate(User u, String cat1, String cat2, String cat3) {
        try (Connection c = DB.getConnection()) {
            if (u.getId()==0) {
                // create
                String salt = genSalt();
                String pwd = u.getSenhaPlain()!=null?u.getSenhaPlain():"usuario123";
                String hash = sha256(salt + pwd);
                PreparedStatement ps = c.prepareStatement("INSERT INTO usuario (nome,idade,tipo,email,senha_hash,salt,ativo) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getNome()); ps.setInt(2, u.getIdade()); ps.setString(3, u.getTipo()); ps.setString(4, u.getEmail());
                ps.setString(5, hash); ps.setString(6, salt); ps.setBoolean(7, u.isAtivo());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                int userId = 0;
                if (keys.next()) userId = keys.getInt(1);
                // insert interests (up to two based on non-null params)
                PreparedStatement pi = c.prepareStatement("INSERT INTO interesse_usuario (usuario_id,categoria) VALUES (?,?)");
                if (cat1!=null) { pi.setInt(1,userId); pi.setString(2,cat1); pi.executeUpdate(); }
                if (cat2!=null) { pi.setInt(1,userId); pi.setString(2,cat2); pi.executeUpdate(); }
                if (cat3!=null) { pi.setInt(1,userId); pi.setString(2,cat3); pi.executeUpdate(); }
            } else {
                // update basic fields
                PreparedStatement ps = c.prepareStatement("UPDATE usuario SET nome=?,idade=?,tipo=?,email=?,ativo=? WHERE id=?");
                ps.setString(1,u.getNome()); ps.setInt(2,u.getIdade()); ps.setString(3,u.getTipo()); ps.setString(4,u.getEmail()); ps.setBoolean(5,u.isAtivo()); ps.setInt(6,u.getId());
                ps.executeUpdate();
                // interests: simple approach - delete existing and insert new (if any)
                PreparedStatement del = c.prepareStatement("DELETE FROM interesse_usuario WHERE usuario_id=?");
                del.setInt(1,u.getId()); del.executeUpdate();
                PreparedStatement pi = c.prepareStatement("INSERT INTO interesse_usuario (usuario_id,categoria) VALUES (?,?)");
                if (cat1!=null) { pi.setInt(1,u.getId()); pi.setString(2,cat1); pi.executeUpdate(); }
                if (cat2!=null) { pi.setInt(1,u.getId()); pi.setString(2,cat2); pi.executeUpdate(); }
                if (cat3!=null) { pi.setInt(1,u.getId()); pi.setString(2,cat3); pi.executeUpdate(); }
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }
    // utils
    private static String sha256(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] b = md.digest(s.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte x: b) sb.append(String.format("%02x", x));
        return sb.toString();
    }
    private static String genSalt() throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte x: salt) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}
