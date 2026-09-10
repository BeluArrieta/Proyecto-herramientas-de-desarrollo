package Modelo;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Registro {

    private final Conexion cn = new Conexion();

    public boolean registrarUsuario(
            String idPersona,
            String nombre,
            String apellido,
            String correo,
            String telefono,
            String usuario,
            String contrasena
    ) {
        String sqlPersona = """
            INSERT INTO persona(
                id_persona,
                nombre,
                apellido,
                fecha_nacimiento,
                correo,
                telefono,
                usuario
            )
            VALUES (?, ?, ?, NULL, ?, ?, ?)
        """;

        String sqlUsuario = """
            INSERT INTO usuario(
                usuario,
                contrasena,
                id_persona
            )
            VALUES (?, ?, ?)
        """;

        String sqlCliente = """
            INSERT INTO cliente(
                id_cliente,
                nombre,
                apellido,
                telefono,
                contrasena,
                correo
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = cn.getConexion()) {
            con.setAutoCommit(false);

            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona);
                 PreparedStatement psUsuario = con.prepareStatement(sqlUsuario);
                 PreparedStatement psCliente = con.prepareStatement(sqlCliente)) {

                psPersona.setString(1, idPersona);
                psPersona.setString(2, nombre);
                psPersona.setString(3, apellido);
                psPersona.setString(4, correo);
                psPersona.setString(5, telefono);
                psPersona.setString(6, usuario);
                psPersona.executeUpdate();

                psUsuario.setString(1, usuario);
                psUsuario.setString(2, contrasena);
                psUsuario.setString(3, idPersona);
                psUsuario.executeUpdate();

                psCliente.setString(1, idPersona);
                psCliente.setString(2, nombre);
                psCliente.setString(3, apellido);
                psCliente.setString(4, telefono);
                psCliente.setString(5, contrasena);
                psCliente.setString(6, correo);
                psCliente.executeUpdate();

                con.commit();
                return true;
            } catch (Exception e) {
                con.rollback();
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
