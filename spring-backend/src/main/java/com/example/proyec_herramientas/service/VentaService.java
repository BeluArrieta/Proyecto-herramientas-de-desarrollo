package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.CarritoRequestDTO;
import com.example.proyec_herramientas.persistence.Venta;

import java.util.List;

public interface VentaService {

    Venta registrarVenta(CarritoRequestDTO carrito);

    Venta obtenerVentaPorId(String idVenta);

    List<Venta> obtenerBoletas();

    List<Venta> obtenerHistorial(String idCliente);
}
