package com.example.proyec_herramientas.model;

public class ItemCarritoDTO {
    private ProductoDTO producto;
    private int cantidad;

    public ItemCarritoDTO(ProductoDTO producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return "ItemCarritoDTO{" +
               "producto=" + producto.getNombre() +
               ", cantidad=" + cantidad +
               ", subtotal=" + getSubtotal() +
               '}';
    }
}