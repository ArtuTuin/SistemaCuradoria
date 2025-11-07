import java.sql.Connection;
import util.ConnectionFactory;

public class TestaConexao {
    public static void main(String[] args) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("✅ Conexão bem-sucedida com o banco de dados!");
        } catch (Exception e) {
            System.out.println("❌ Falha na conexão: " + e.getMessage());
        }
    }
}
