package com.example.proyec_herramientas.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medio_pago")
public class MedioPago {

    @Id
    @Column(name = "id_medio_pago", length = 10, nullable = false)
    private String idMedioPago;

    @Column(name = "descripcion", length = 100, nullable = false)
    private String descripcion;

    public MedioPago() {
    }

    public MedioPago(String idMedioPago, String descripcion) {
        this.idMedioPago = idMedioPago;
        this.descripcion = descripcion;
    }

    public String getIdMedioPago() {
        return idMedioPago;
    }

    public void setIdMedioPago(String idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
