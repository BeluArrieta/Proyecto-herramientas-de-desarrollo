package com.example.proyec_herramientas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    private String contrasena;

    @Field("id_persona")
    private String idPersona;

    public Usuario() {
    }

    public Usuario(String id, String contrasena, String idPersona) {
        this.id = id;
        this.contrasena = contrasena;
        this.idPersona = idPersona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
}