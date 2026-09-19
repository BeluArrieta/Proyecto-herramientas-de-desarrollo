package ModeloDAO;

import Config.ConexionMongo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ClienteDAO {

    private final MongoCollection<Document> coleccion = ConexionMongo.getColeccion("clientes");

    // =========================
    // TOTAL CLIENTES
    // =========================
    public int obtenerTotalClientes() {

        try {
            return (int) coleccion.countDocuments();
        } catch (Exception e) {
            System.out.println("Error obtener total clientes: " + e);
        }

        return 0;
    }
}