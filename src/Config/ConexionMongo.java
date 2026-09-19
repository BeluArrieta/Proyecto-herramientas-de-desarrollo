package Config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionMongo {

    private static final String URI = System.getProperty("mongo.uri",
            System.getenv().getOrDefault("MONGO_URI",
                    "mongodb://admin:123@localhost:27017/?authSource=admin"));
    private static final String NOMBRE_BD = System.getProperty("mongo.db", "proyecto");

    private static final MongoClient cliente;
    private static final MongoDatabase base;

    static {
        cliente = MongoClients.create(URI);
        base = cliente.getDatabase(NOMBRE_BD);
    }

    public static MongoCollection<Document> getColeccion(String nombre) {
        return base.getCollection(nombre);
    }

    public static void cerrar() {
        cliente.close();
    }

    public static void main(String[] args) {
        try {
            System.out.println("Colecciones en '" + NOMBRE_BD + "':");
            for (String nombre : base.listCollectionNames()) {
                System.out.println("  - " + nombre);
            }
        } finally {
            cerrar();
        }
    }
}
