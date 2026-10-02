package com.example.proyec_herramientas.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_documento")
public class TipoDocumento {

    @Id
    @Column(name = "id_documento", length = 25, nullable = false)
    private String idDocumento;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    public TipoDocumento() {
    }

    public TipoDocumento(String idDocumento, String nombre) {
        this.idDocumento = idDocumento;
        this.nombre = nombre;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
