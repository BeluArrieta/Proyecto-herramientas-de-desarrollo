package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, String> {

    List<Venta> findByIdTipoDocumentoOrderByFechaEmisionDesc(String idTipoDocumento);

    List<Venta> findByIdClienteOrderByFechaEmisionDesc(String idCliente);

    List<Venta> findAllByOrderByFechaEmisionDesc();
}
