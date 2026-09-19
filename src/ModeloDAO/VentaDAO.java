package ModeloDAO;

import Config.ConexionMongo;
import com.mongodb.client.MongoCollection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.bson.Document;

public class VentaDAO {

    private final MongoCollection<Document> coleccion = ConexionMongo.getColeccion("ventas");

    // ==========================================
    // PRODUCTO MÁS VENDIDO DEL MES
    // Usa agregación: desarma detalles -> agrupa por producto
    // ==========================================
    public String obtenerProductoMasVendidoMes() {

        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fin = inicio.plusMonths(1);

        Date fechaInicio = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaFin = Date.from(fin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Document> pipeline = List.of(
                new Document("$match",
                        new Document("fecha_emision",
                                new Document("$gte", fechaInicio)
                                        .append("$lt", fechaFin))),
                new Document("$unwind", "$detalles"),
                new Document("$group",
                        new Document("_id", "$detalles.id_producto")
                                .append("nombre", new Document("$first", "$detalles.producto"))
                                .append("total", new Document("$sum", "$detalles.cantidad"))),
                new Document("$sort", new Document("total", -1)),
                new Document("$limit", 1)
        );

        try {
            Document doc = coleccion.aggregate(pipeline).first();

            if (doc != null) {
                return doc.getString("nombre");
            }

        } catch (Exception e) {
            System.out.println("Error producto mas vendido mes: " + e);
        }

        return "Sin ventas";
    }
}