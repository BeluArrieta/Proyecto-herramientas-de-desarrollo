package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}