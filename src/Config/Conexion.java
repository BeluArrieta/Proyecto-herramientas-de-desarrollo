package Config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    Connection con;

    public Connection getConexion() {

        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/proyecto",
                    "postgres",
                    "123456"
            );

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e);
        }

        return con;
    }
}