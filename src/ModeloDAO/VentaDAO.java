package ModeloDAO;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentaDAO {

    Conexion cn = new Conexion();

    public String obtenerProductoMasVendidoMes() {

        String sql = """
            SELECT p.nombre, SUM(dv.cantidad) AS total_vendido
            FROM detalle_venta dv
            INNER JOIN producto p ON dv.id_producto = p.id_producto
            INNER JOIN venta v ON dv.id_venta = v.id_venta
            WHERE EXTRACT(MONTH FROM v.fecha_emision) = EXTRACT(MONTH FROM CURRENT_DATE)
            AND EXTRACT(YEAR FROM v.fecha_emision) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY p.nombre
            ORDER BY total_vendido DESC
            LIMIT 1
        """;

        try (
                Connection con = cn.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getString("nombre");
            }

        } catch (Exception e) {
            System.out.println("Error obtener producto mas vendido del mes: " + e);
        }

        return "Sin ventas";
    }
}