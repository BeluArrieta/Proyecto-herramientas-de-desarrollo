package Modelo;
// ProductoDigital.java

import Abstrac.Producto;

public class ProductoDigital extends Producto {

    public ProductoDigital(String id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public String tipoProducto() {
        return "Digital";
    }
}
