package ModeloDAO;

import Config.ConexionMongo;
import ModeloDTO.ProductoDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import org.bson.Document;

public class ProductoDAO {

    private final MongoCollection<Document> coleccion = ConexionMongo.getColeccion("productos");

    // =========================
    // LISTAR TODO (catálogo)
    // =========================
    public ArrayList<ProductoDTO> listarTodo() {

        ArrayList<ProductoDTO> lista = new ArrayList<>();

        try {
            for (Document doc : coleccion.find().sort(new Document("nombre", 1))) {
                lista.add(toProducto(doc));
            }
        } catch (Exception e) {
            System.out.println("Error listar productos: " + e);
        }

        return lista;
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public ProductoDTO buscarPorId(int idProducto) {

        try {
            Document doc = coleccion.find(Filters.eq("_id", idProducto)).first();

            if (doc != null) {
                return toProducto(doc);
            }

        } catch (Exception e) {
            System.out.println("Error buscar producto: " + e);
        }

        return null;
    }

    // =========================
    // AGREGAR PRODUCTO (admin)
    // =========================
    public boolean agregar(ProductoDTO producto) {

        try {
            Document doc = new Document("_id", producto.getIdProducto())
                    .append("nombre", producto.getNombre())
                    .append("stock", producto.getStock())
                    .append("precio", producto.getPrecio())
                    .append("categoria", producto.getCategoria());

            coleccion.insertOne(doc);
            return true;

        } catch (Exception e) {
            System.out.println("Error agregar producto: " + e);
        }

        return false;
    }

    // =========================
    // ACTUALIZAR PRODUCTO (admin)
    // =========================
    public boolean actualizar(ProductoDTO producto) {

        try {
            UpdateResult res = coleccion.updateOne(
                    Filters.eq("_id", producto.getIdProducto()),
                    new Document("$set",
                            new Document("nombre", producto.getNombre())
                                    .append("stock", producto.getStock())
                                    .append("precio", producto.getPrecio())
                                    .append("categoria", producto.getCategoria()))
            );

            return res.getModifiedCount() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizar producto: " + e);
        }

        return false;
    }

    // =========================
    // ACTUALIZAR STOCK
    // =========================
    public boolean actualizarStock(int idProducto, int cantidadComprada) {

        try {
            UpdateResult res = coleccion.updateOne(
                    Filters.eq("_id", idProducto),
                    new Document("$inc", new Document("stock", -cantidadComprada))
            );

            return res.getModifiedCount() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizar stock: " + e);
        }

        return false;
    }

    // =========================
    // OBTENER PRODUCTOS AGOTADOS
    // =========================
    public int obtenerProductosAgotados() {

        try {
            return (int) coleccion.countDocuments(new Document("stock", new Document("$lte", 0)));
        } catch (Exception e) {
            System.out.println("Error productos agotados: " + e);
        }

        return 0;
    }

    // =========================
    // MAPEO Document -> ProductoDTO
    // =========================
    private ProductoDTO toProducto(Document doc) {

        ProductoDTO p = new ProductoDTO();

        Object id = doc.get("_id");
        p.setIdProducto(id instanceof Number ? ((Number) id).intValue() : 0);
        p.setNombre(doc.getString("nombre"));
        p.setStock(doc.getInteger("stock", 0));
        p.setPrecio(doc.getDouble("precio") != null ? doc.getDouble("precio") : 0.0);
        p.setCategoria(doc.getString("categoria"));

        return p;
    }
}