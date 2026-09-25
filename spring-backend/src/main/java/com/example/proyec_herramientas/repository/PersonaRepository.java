package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.model.Persona;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonaRepository extends MongoRepository<Persona, String> {

    Persona findByCorreo(String correo);

    long countByCorreo(String correo);
}