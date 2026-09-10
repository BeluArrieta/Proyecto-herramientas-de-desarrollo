package ModeloDTO;

public class ItemCarritoDTO {
    private ProductoDTO producto;
    private int cantidad;

    public ItemCarritoDTO(ProductoDTO producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Método para calcular el subtotal de este ítem
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    // Opcional: Para depuración
    @Override
    public String toString() {
        return "ItemCarritoDTO{" +
               "producto=" + producto.getNombre() +
               ", cantidad=" + cantidad +
               ", subtotal=" + getSubtotal() +
               '}';
    }
}