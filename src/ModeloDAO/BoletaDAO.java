package ModeloDAO;

import Config.ConexionMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;

public class BoletaDAO {

    private final MongoCollection<Document> coleccion = ConexionMongo.getColeccion("ventas");

    // ==========================================
    // HISTORIAL DE COMPRAS DE UN CLIENTE
    // ==========================================
    public ArrayList<Object[]> obtenerHistorialCompras(String idCliente) {

        ArrayList<Object[]> lista = new ArrayList<>();

        try {
            List<Document> ventas = coleccion
                    .find(Filters.eq("id_persona", idCliente))
                    .sort(new Document("fecha_emision", -1))
                    .into(new ArrayList<>());

            for (Document venta : ventas) {
                lista.addAll(aplanarVenta(venta));
            }

        } catch (Exception e) {
            System.out.println("Error historial: " + e);
        }

        return lista;
    }

    // ==========================================
    // HISTORIAL GENERAL DE TODOS LOS CLIENTES
    // ==========================================
    public ArrayList<Object[]> obtenerHistorialGeneral(String filtro) {

        ArrayList<Object[]> lista = new ArrayList<>();

        try {
            List<Document> ventas = coleccion
                    .find()
                    .sort(new Document("fecha_emision", -1))
                    .into(new ArrayList<>());

            for (Document venta : ventas) {

                String cliente = venta.getString("cliente");
                String documento = venta.getString("tipo_documento");
                String medioPago = venta.getString("medio_pago");
                String id = venta.getString("_id");

                for (Document detalle : venta.getList("detalles", Document.class)) {

                    String producto = detalle.getString("producto");

                    if (coincideFiltro(filtro, id, cliente, documento, medioPago, producto)) {
                        lista.add(new Object[]{
                            id,
                            venta.getDate("fecha_emision"),
                            cliente,
                            documento,
                            medioPago,
                            producto,
                            detalle.getInteger("cantidad"),
                            detalle.getDouble("precio_unitario"),
                            detalle.getDouble("subtotal")
                        });
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error historial general: " + e);
        }

        return lista;
    }

    // ==========================================
    // APLANA UNA VENTA EN FILAS (una por producto)
    // ==========================================
    private List<Object[]> aplanarVenta(Document venta) {

        List<Object[]> filas = new ArrayList<>();

        String id = venta.getString("_id");
        Date fecha = venta.getDate("fecha_emision");
        String documento = venta.getString("tipo_documento");

        for (Document detalle : venta.getList("detalles", Document.class)) {
            filas.add(new Object[]{
                id,
                fecha,
                documento,
                detalle.getString("producto"),
                detalle.getInteger("cantidad"),
                detalle.getDouble("precio_unitario"),
                detalle.getDouble("subtotal")
            });
        }

        return filas;
    }

    // ==========================================
    // FILTRO POR TEXTO (insensible a mayúsculas)
    // ==========================================
    private boolean coincideFiltro(String filtro, String... campos) {

        if (filtro == null || filtro.trim().isEmpty()) {
            return true;
        }

        String busqueda = filtro.trim().toLowerCase();

        for (String campo : campos) {
            if (campo != null && campo.toLowerCase().contains(busqueda)) {
                return true;
            }
        }

        return false;
    }
}