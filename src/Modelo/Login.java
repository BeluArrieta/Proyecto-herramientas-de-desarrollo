package Modelo;

import Config.ConexionMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.Map;
import org.bson.Document;

public class Login {

    private final MongoCollection<Document> usuarios = ConexionMongo.getColeccion("usuarios");
    private final MongoCollection<Document> personas = ConexionMongo.getColeccion("personas");
    private final MongoCollection<Document> clientes = ConexionMongo.getColeccion("clientes");

    // ==========================================
    // AUTENTICAR: devuelve datos de persona + tipo
    // de usuario ("ADMIN" / "CLIENTE") o null si falla
    // ==========================================
    public Map<String, String> autenticar(String usuarioIngresado, String contrasenaIngresada) {

        try {

            Document usuario = usuarios.find(Filters.eq("_id", usuarioIngresado)).first();

            if (usuario == null) {
                return null;
            }

            String contrasenaCifrada = usuario.getString("contrasena");

            if (!SeguridadContrasena.verificar(contrasenaIngresada, contrasenaCifrada)) {
                return null;
            }

            String idPersona = usuario.getString("id_persona");

            Document persona = idPersona != null
                    ? personas.find(Filters.eq("_id", idPersona)).first()
                    : null;

            if (persona == null) {
                return null;
            }

            String nombre = persona.getString("nombre");
            String apellido = persona.getString("apellido");
            String correo = persona.getString("correo");
            String telefono = persona.getString("telefono");

            boolean esCliente = esClienteRegistrado(idPersona, correo);

            return Map.of(
                    "id_persona", idPersona,
                    "nombre", nombre != null ? nombre : "",
                    "apellido", apellido != null ? apellido : "",
                    "correo", correo != null ? correo : "",
                    "telefono", telefono != null ? telefono : "",
                    "tipo_usuario", esCliente ? "CLIENTE" : "ADMIN"
            );

        } catch (Exception e) {
            System.out.println("Error login: " + e);
        }

        return null;
    }

    private boolean esClienteRegistrado(String idPersona, String correo) {

        Document cliente = correo != null
                ? clientes.find(
                        Filters.or(
                                Filters.eq("_id", idPersona),
                                Filters.eq("correo", correo)
                        )
                ).first()
                : clientes.find(Filters.eq("_id", idPersona)).first();

        return cliente != null;
    }
}