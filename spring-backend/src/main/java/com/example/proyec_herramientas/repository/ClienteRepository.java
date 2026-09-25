package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

    boolean existsByCorreo(String correo);
}