package dao;
import db.DB;
import model.Resource;
import java.sql.*;
import java.util.*;
public class ResourceDAO {
    public static void save(Resource r) {
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO recurso (usuario_id,titulo,autor,categoria) VALUES (?,?,?,?)");
            ps.setInt(1, r.getUsuarioId());
            ps.setString(2, r.getTitulo());
            ps.setString(3, r.getAutor());
            ps.setString(4, r.getCategoria());
            ps.executeUpdate();
        } catch(Exception ex){ ex.printStackTrace(); }
    }
    public static List<Resource> findByUser(int userId) {
        List<Resource> list = new ArrayList<>();
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT id,usuario_id,titulo,autor,categoria FROM recurso WHERE usuario_id=?");
            ps.setInt(1,userId);
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
        } catch(Exception ex){ ex.printStackTrace(); }
        return list;
    }
}
