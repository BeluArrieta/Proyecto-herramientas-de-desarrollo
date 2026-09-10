package Modelo;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    Conexion cn = new Conexion();

    public boolean validarLogin(String usuarioIngresado, String contrasenaIngresada) {

        String sql = """
            SELECT u.contrasena
            FROM usuario
            WHERE usuario = ?
        """;

        try (
                Connection con = cn.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, usuarioIngresado);

            ResultSet rs = ps.executeQuery();

            return rs.next() && SeguridadContrasena.verificar(contrasenaIngresada, rs.getString("contrasena"));

        } catch (Exception e) {
            System.out.println("Error login: " + e);
        }

        return false;
    }
}