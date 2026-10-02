package com.example.proyec_herramientas.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "venta")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venta {

    @Id
    @Column(name = "id_venta", length = 36, nullable = false)
    private String idVenta;

    @Column(name = "id_cliente", length = 20, nullable = false)
    private String idCliente;

    @Column(name = "id_persona", length = 25, nullable = false)
    private String idPersona;

    @Column(name = "id_tipo_documento", length = 25, nullable = false)
    private String idTipoDocumento;

    @Column(name = "numero_documento", length = 36, nullable = false)
    private String numeroDocumento;

    @Column(name = "id_medio_pago", length = 10, nullable = false)
    private String idMedioPago;

    @Column(name = "fecha_emision", nullable = false)
    private Date fechaEmision;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idDetalle")
    private List<DetalleVenta> detalles = new ArrayList<>();

    @Transient
    private String cliente;

    @Transient
    private String tipoDocumento;

    @Transient
    private String medioPago;

    public Venta() {
    }

    public String getId() {
        return idVenta;
    }

    public void setId(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public String getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(String idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getIdMedioPago() {
        return idMedioPago;
    }

    public void setIdMedioPago(String idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        return detalles == null ? 0d : detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalle.setVenta(this);
        detalles.add(detalle);
    }
}
