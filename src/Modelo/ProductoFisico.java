package Modelo;
// ProductoFisico.java

import Abstrac.Producto;

public class ProductoFisico extends Producto {

    private int idProducto;
    private String nombre;
    private int stock;
    private double precio;
    private String categoria;

    public ProductoFisico(String id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public String tipoProducto() {
        return "Físico";
    }
}
