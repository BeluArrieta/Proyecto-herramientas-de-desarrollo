package com.example.proyec_herramientas.controller;

import com.example.proyec_herramientas.exception.RecursoNoEncontradoException;
import com.example.proyec_herramientas.model.ProductoDTO;
import com.example.proyec_herramientas.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ProductoDTO obtener(@PathVariable int id) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto " + id + " no existe");
        }
        return producto;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable int id, @RequestBody ProductoDTO producto) {
        ProductoDTO actualizado = productoService.actualizar(id, producto);
        if (actualizado == null) {
            throw new RecursoNoEncontradoException("Producto " + id + " no existe");
        }
        return actualizado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (productoService.obtenerPorId(id) == null) {
            throw new RecursoNoEncontradoException("Producto " + id + " no existe");
        }
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}