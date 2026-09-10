package Abstrac;
// Producto.java
public abstract class Producto {
    private String id;
    private String nombre;
    private double precio;
    
    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    
    public abstract String tipoProducto();
}