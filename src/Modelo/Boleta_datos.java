package Modelo;

public class Boleta_datos{

    private double total = 0.0;
    private double precio = 0.0;
    private int cantidad = 0;
    private double subtotal = 0.0;

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void agregarProducto() {

        subtotal = precio * cantidad;
        total += subtotal;

    }

}
