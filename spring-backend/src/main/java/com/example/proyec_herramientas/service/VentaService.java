package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.CarritoRequestDTO;
import com.example.proyec_herramientas.persistence.VentaDocument;

import java.util.List;

public interface VentaService {

    VentaDocument registrarVenta(CarritoRequestDTO carrito);

    VentaDocument obtenerVentaPorId(String idVenta);

    List<VentaDocument> obtenerBoletas();

    List<VentaDocument> obtenerHistorial(String idCliente);
}