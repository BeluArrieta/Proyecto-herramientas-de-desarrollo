package ModeloDAO;

import Config.ConexionMongo;
import ModeloDTO.ProductoDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.bson.Document;

public class FacturaDAO {

    private final MongoCollection<Document> coleccionVentas = ConexionMongo.getColeccion("ventas");

    // ==========================================
    // REGISTRAR VENTA (documento "ventas")
    // ==========================================
    public String registrarVenta(
            String idPersona,
            String nombreCliente,
            String tipoDocumento,
            String numeroDocumento,
            String medioPago
    ) {

        String idVenta = UUID.randomUUID().toString();

        Document doc = new Document("_id", idVenta)
                .append("id_persona", idPersona)
                .append("cliente", nombreCliente)
                .append("id_tipo_documento", idTipoDocumento(tipoDocumento))
                .append("tipo_documento", tipoDocumento)
                .append("numero_documento", numeroDocumento)
                .append("id_medio_pago", idMedioPago(medioPago))
                .append("medio_pago", medioPago)
                .append("fecha_emision", new Date())
                .append("total", 0.0)
                .append("detalles", new ArrayList<>());

        try {
            coleccionVentas.insertOne(doc);
            return idVenta;

        } catch (Exception e) {
            System.out.println("Error registrar venta: " + e);
        }

        return null;
    }

    // ==========================================
    // REGISTRAR DETALLE (agrega item a "detalles" y acumula total)
    // ==========================================
    public boolean registrarDetalle(
            String idVenta,
            int idProducto,
            int cantidad,
            double precio,
            double subtotal
    ) {

        String productoNombre = obtenerNombreProducto(idProducto);

        Document detalle = new Document("id_producto", idProducto)
                .append("producto", productoNombre)
                .append("cantidad", cantidad)
                .append("precio_unitario", precio)
                .append("subtotal", subtotal);

        try {

            UpdateResult res = coleccionVentas.updateOne(
                    Filters.eq("_id", idVenta),
                    new Document("$push", new Document("detalles", detalle))
                            .append("$inc", new Document("total", subtotal))
            );

            return res.getModifiedCount() > 0;

        } catch (Exception e) {
            System.out.println("Error detalle venta: " + e);
        }

        return false;
    }

    // ==========================================
    // BUSCAR PRODUCTO PARA el nombre en el detalle
    // ==========================================
    private String obtenerNombreProducto(int idProducto) {

        ProductoDTO producto = new ProductoDAO().buscarPorId(idProducto);

        if (producto != null) {
            return producto.getNombre();
        }

        return "Producto " + idProducto;
    }

    // ==========================================
    // MAPEO id de tipo de documento
    // ==========================================
    private String idTipoDocumento(String tipo) {
        return "Factura".equalsIgnoreCase(tipo) ? "DOC-02" : "DOC-01";
    }

    // ==========================================
    // MAPEO id de medio de pago
    // ==========================================
    private String idMedioPago(String medioPago) {

        switch (medioPago) {
            case "Tarjeta":
            case "Tarjeta de crédito":
            case "Visa":
            case "Mastercard":
                return "MP002";
            case "Tarjeta de débito":
                return "MP003";
            case "Yape":
                return "MP004";
            case "Plin":
                return "MP005";
            case "Transferencia":
            case "Depósito":
                return "MP006";
            default:
                return "MP001";
        }
    }
}