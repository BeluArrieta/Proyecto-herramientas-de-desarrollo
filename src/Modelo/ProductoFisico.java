package Modelo;
// ProductoFisico.java

import Abstrac.Producto;

public class ProductoFisico extends Producto {

    public ProductoFisico(String id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public String tipoProducto() {
        return "Físico";
    }
}
