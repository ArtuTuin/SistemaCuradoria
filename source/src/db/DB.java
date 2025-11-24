package db;
import java.sql.*;
public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/curadoria?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // ajuste sua senha
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
