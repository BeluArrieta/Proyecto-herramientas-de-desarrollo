package com.example.proyec_herramientas.controller;

import com.example.proyec_herramientas.model.CarritoRequestDTO;
import com.example.proyec_herramientas.persistence.VentaDocument;
import com.example.proyec_herramientas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaDocument> registrar(@Valid @RequestBody CarritoRequestDTO carrito) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrarVenta(carrito));
    }
}