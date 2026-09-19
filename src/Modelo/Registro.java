package Modelo;

import Config.ConexionMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class Registro {

    private final MongoCollection<Document> personas = ConexionMongo.getColeccion("personas");
    private final MongoCollection<Document> usuarios = ConexionMongo.getColeccion("usuarios");
    private final MongoCollection<Document> clientes = ConexionMongo.getColeccion("clientes");

    public boolean registrarUsuario(
            String idPersona,
            String nombre,
            String apellido,
            String correo,
            String telefono,
            String usuario,
            String contrasena
    ) {

        String contrasenaCifrada = SeguridadContrasena.hash(contrasena);

        try {

            personas.insertOne(new Document("_id", idPersona)
                    .append("nombre", nombre)
                    .append("apellido", apellido)
                    .append("fecha_nacimiento", null)
                    .append("correo", correo)
                    .append("telefono", telefono)
                    .append("usuario", usuario));

            usuarios.insertOne(new Document("_id", usuario)
                    .append("contrasena", contrasenaCifrada)
                    .append("id_persona", idPersona));

            clientes.insertOne(new Document("_id", idPersona)
                    .append("nombre", nombre)
                    .append("apellido", apellido)
                    .append("telefono", telefono)
                    .append("correo", correo)
                    .append("contrasena", contrasenaCifrada));

            return true;

        } catch (Exception e) {
            System.out.println("Error registrar usuario: " + e);
            deshacerRegistro(idPersona, usuario);
        }

        return false;
    }

    // Mejor esfuerzo: si algo falla, se eliminan los documentos ya insertados
    private void deshacerRegistro(String idPersona, String usuario) {

        try {
            personas.deleteOne(Filters.eq("_id", idPersona));
            usuarios.deleteOne(Filters.eq("_id", usuario));
            clientes.deleteOne(Filters.eq("_id", idPersona));
        } catch (Exception e) {
            System.out.println("Error al deshacer registro: " + e);
        }
    }
}