package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listar();

    ProductoDTO obtenerPorId(int idProducto);

    ProductoDTO crear(ProductoDTO producto);

    ProductoDTO actualizar(int idProducto, ProductoDTO producto);

    void eliminar(int idProducto);

    boolean actualizarStock(int idProducto, int cantidad);
}