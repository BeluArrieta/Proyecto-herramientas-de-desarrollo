package ModeloDAO;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class FacturaDAO {

    Conexion cn = new Conexion();

    public String registrarVenta(
            String idPersona,
            String tipoDocumento,
            String numeroDocumento,
            String medioPago
    ) {

        String idVenta = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO venta(
                id_venta,
                id_persona,
                id_tipo_documento,
                numero_documento,
                id_medio_pago,
                fecha_emision
            )
            VALUES (?, ?, ?, ?, ?, NOW())
        """;

        try (
                Connection con = cn.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            // Se asignan los valores en el orden correcto de los '?'
            ps.setString(1, idVenta);
            ps.setString(2, idPersona);
            ps.setString(3, tipoDocumento);
            ps.setString(4, numeroDocumento);
            ps.setString(5, medioPago);

            ps.executeUpdate();

            return idVenta;

        } catch (Exception e) {
            System.out.println("Error registrar venta: " + e);
        }

        return null;
    }

    public boolean registrarDetalle(
            String idVenta,
            int idProducto,
            int cantidad,
            double precio,
            double subtotal
    ) {

        String sql = """
            INSERT INTO detalle_venta(
                id_venta,
                id_producto,
                cantidad,
                precio_unitario,
                subtotal
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection con = cn.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, idVenta);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.setDouble(4, precio);
            ps.setDouble(5, subtotal);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error detalle venta: " + e);
        }
        return false;
    }
}