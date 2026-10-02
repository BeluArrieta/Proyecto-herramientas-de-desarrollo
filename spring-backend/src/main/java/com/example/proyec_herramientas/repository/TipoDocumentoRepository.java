package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, String> {

    Optional<TipoDocumento> findByNombreIgnoreCase(String nombre);
}
