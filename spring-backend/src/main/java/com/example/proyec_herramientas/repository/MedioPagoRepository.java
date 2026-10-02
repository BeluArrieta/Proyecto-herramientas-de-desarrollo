package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedioPagoRepository extends JpaRepository<MedioPago, String> {

    Optional<MedioPago> findByDescripcionIgnoreCase(String descripcion);
}
