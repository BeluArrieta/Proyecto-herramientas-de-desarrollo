package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.VentaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VentaRepository extends MongoRepository<VentaDocument, String> {

    List<VentaDocument> findByTipoDocumentoOrderByFechaEmisionDesc(String tipoDocumento);

    List<VentaDocument> findByIdPersonaOrderByFechaEmisionDesc(String idPersona);

    List<VentaDocument> findAllByOrderByFechaEmisionDesc();
}