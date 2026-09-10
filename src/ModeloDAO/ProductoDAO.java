package ModeloDAO;

import Config.Conexion;
import ModeloDTO.ProductoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductoDAO {

    Conexion cn = new Conexion();
    private ArrayList<ProductoDTO> productosCatalogo;

    public ResultSet listarProductos() {

        String sql = "SELECT * FROM producto ORDER BY nombre";

        try {
            Connection con = cn.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            return ps.executeQuery();

        } catch (Exception e) {
            System.out.println("Error listar productos: " + e);
        }

        return null;
    }

    public boolean actualizarStock(int idProducto, int cantidadComprada) {

        String sql = """
            UPDATE producto
            SET stock = stock - ?
            WHERE id_producto = ?
        """;

        try (
                Connection con = cn.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidadComprada);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizar stock: " + e);
        }

        return false;
    }

    public ProductoDTO buscarPorId(int idProducto) {

        String sql = "SELECT * FROM producto WHERE id_producto = ?";

        ProductoDTO producto = null;

        try (
                Connection con = cn.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                producto = new ProductoDTO();

                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setCategoria(rs.getString("categoria"));
            }

        } catch (Exception e) {
            System.out.println("Error buscar producto: " + e);
        }

        return producto;
    }

    public ArrayList<ProductoDTO> listarTodo() {

        ArrayList<ProductoDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM producto ORDER BY nombre";

        try (
                Connection con = cn.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ProductoDTO p = new ProductoDTO();

                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("stock"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCategoria(rs.getString("categoria"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error listar productos: " + e);
        }

        return lista;
    }

    public int obtenerProductosAgotados() {

        String sql = "SELECT COUNT(*) AS total FROM producto WHERE stock <= 0";

        try (
                Connection con = cn.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.out.println("Error obtener productos agotados: " + e);
        }

        return 0;
    }
}
