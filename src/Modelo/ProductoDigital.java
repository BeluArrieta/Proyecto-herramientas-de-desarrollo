package Modelo;
// ProductoDigital.java

import Abstrac.Producto;

public class ProductoDigital extends Producto {

    private int idProducto;
    private String nombre;
    private int stock;
    private double precio;
    private String categoria;

    public ProductoDigital(String id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public String tipoProducto() {
        return "Digital";
    }
}
