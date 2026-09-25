package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.ClienteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<ClienteDocument, String> {
}