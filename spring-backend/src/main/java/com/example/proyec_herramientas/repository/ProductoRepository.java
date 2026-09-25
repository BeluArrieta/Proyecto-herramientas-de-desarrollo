package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.ProductoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository extends MongoRepository<ProductoDocument, Integer> {
}