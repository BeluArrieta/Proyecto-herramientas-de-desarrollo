package ModeloDAO;

import ModeloDTO.ClienteDTO;
import Miinterfaces.ClienteInterface;
import Config.Conexion;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.security.MessageDigest;

public class ClienteDAO implements ClienteInterface {

    private static final String SALT = "TuSaltSecreto123!";
    Conexion con = new Conexion();

    // =========================
    // HASH SHA-256
    // =========================
    private String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // AGREGAR CLIENTE
    // =========================
    @Override
    public boolean agregar(ClienteDTO cliente) {

        String sql = "INSERT INTO cliente (id_cliente, nombre, apellido, telefono, contrasena, correo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = con.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String hash = hashSHA256(SALT + cliente.getPassword());

            ps.setString(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, hash);
            ps.setString(6, cliente.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public ClienteDTO buscarClienteParaLogin(String idCliente, String password) {

        ClienteDTO c = null;

        String sql = "SELECT id_cliente, nombre, apellido, telefono, correo "
                + "FROM cliente "
                + "WHERE id_cliente = ? AND contrasena = ?";

        try (Connection conn = con.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String hash = hashSHA256(SALT + password);

            ps.setString(1, idCliente);
            ps.setString(2, hash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new ClienteDTO();

                    c.setIdCliente(rs.getString("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreo(rs.getString("correo"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error en login: " + e.getMessage());
        }

        return c;
    }
// =========================
// LISTAR UNO
// =========================

    @Override
    public ClienteDTO listarUno(String id) {

        ClienteDTO c = null;

        String sql = "SELECT id_cliente, nombre, apellido, telefono, correo "
                + "FROM cliente WHERE id_cliente = ?";

        try (Connection conn = con.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new ClienteDTO();

                    c.setIdCliente(rs.getString("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreo(rs.getString("correo"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar: " + e.getMessage());
        }

        return c;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    @Override
    public ArrayList<ClienteDTO> listarTodo() {

        ArrayList<ClienteDTO> lista = new ArrayList<>();

        String sql = "SELECT id_cliente, nombre, apellido, telefono, correo FROM cliente";

        try (Connection conn = con.getConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClienteDTO c = new ClienteDTO();

                c.setIdCliente(rs.getString("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));

                lista.add(c);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al listar: " + e.getMessage());
        }

        return lista;
    }

    public int obtenerTotalClientes() {

        String sql = "SELECT COUNT(*) AS total FROM cliente";

        try (
                Connection conn = con.getConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.out.println("Error obtener total clientes: " + e);
        }

        return 0;
    }
}
