package Modelo;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    Conexion cn = new Conexion();

    public boolean validarLogin(String usuarioIngresado, String contrasenaIngresada) {

        String sql = """
            SELECT *
            FROM usuario
            WHERE usuario = ?
            AND contrasena = ?
        """;

        try (
                Connection con = cn.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, usuarioIngresado);
            ps.setString(2, contrasenaIngresada);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println("Error login: " + e);
        }

        return false;
    }
}