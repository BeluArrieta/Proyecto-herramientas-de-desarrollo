package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Optional<Cliente> findByCorreo(String correo);
}
