package dao;

import db.DB;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ------------------- INSERT -------------------

    public static void insert(Product p) {
        try (Connection c = DB.getConnection()) {
            String sql = "INSERT INTO product (nome, preco, estoque) VALUES (?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setInt(3, p.getEstoque());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ------------------- FIND ALL -------------------

    public static List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (Connection c = DB.getConnection()) {
            String sql = "SELECT * FROM product";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("estoque")
                );
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ------------------- FIND BY ID -------------------

    public static Product findById(int id) {
        try (Connection c = DB.getConnection()) {
            String sql = "SELECT * FROM product WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("estoque")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ------------------- UPDATE -------------------

    public static void update(Product p) {
        try (Connection c = DB.getConnection()) {
            String sql = "UPDATE product SET nome = ?, preco = ?, estoque = ? WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setInt(3, p.getEstoque());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ------------------- DELETE -------------------

    public static void delete(int id) {
        try (Connection c = DB.getConnection()) {
            String sql = "DELETE FROM product WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
