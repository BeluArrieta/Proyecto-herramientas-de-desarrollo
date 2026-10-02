package com.example.proyec_herramientas.repository;

import com.example.proyec_herramientas.persistence.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
