package ModeloDAO;

import Config.Conexion;
import java.sql.*;
import java.util.ArrayList;

public class BoletaDAO {

    Conexion cn = new Conexion();

    public ResultSet obtenerVenta(String idVenta) {

        String sql = """
            SELECT
                v.id_venta,
                v.fecha_emision,
                p.nombre,
                p.apellido,
                td.nombre AS documento,
                mp.descripcion AS pago
            FROM venta v
            INNER JOIN persona p
                ON v.id_persona = p.id_persona
            INNER JOIN tipo_documento td
                ON v.id_tipo_documento = td.id_documento
            INNER JOIN medio_pago mp
                ON v.id_medio_pago = mp.id_medio_pago
            WHERE v.id_venta = ?
        """;

        try {

            Connection con = cn.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, idVenta);

            return ps.executeQuery();

        } catch (Exception e) {
            System.out.println("Error obtener venta: " + e);
        }

        return null;
    }

    public ArrayList<Object[]> obtenerHistorialCompras(String idCliente) {

        ArrayList<Object[]> lista = new ArrayList<>();

        String sql = """
        SELECT
            v.id_venta,
            v.fecha_emision,
            td.nombre,
            p.nombre,
            dv.cantidad,
            dv.precio_unitario,
            dv.subtotal
        FROM venta v
        INNER JOIN detalle_venta dv
            ON v.id_venta = dv.id_venta
        INNER JOIN producto p
            ON dv.id_producto = p.id_producto
        INNER JOIN tipo_documento td
            ON v.id_tipo_documento = td.id_documento
        WHERE v.id_persona = ?
        ORDER BY v.fecha_emision DESC
    """;

        try (
                Connection con = cn.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idCliente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getTimestamp(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    "S/ " + rs.getDouble(6),
                    "S/ " + rs.getDouble(7)
                });
            }

        } catch (Exception e) {
            System.out.println("Error historial: " + e);
        }

        return lista;
    }
}
