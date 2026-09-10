package Miinterfaces;
// ICarrito.java

import Abstrac.Producto;

public interface ICarrito {
    void agregarProducto(Producto p);
    void eliminarProducto(int index);
}
