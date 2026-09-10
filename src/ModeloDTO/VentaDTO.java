package ModeloDTO;

public class VentaDTO {

    private String idVenta;
    private String facturaId;
    private int productoId;
    private int cantidad;
    private double precioUnitario;
    private ProductoDTO producto;

    public VentaDTO() {
    }

    public VentaDTO(ProductoDTO producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.productoId = producto.getIdProducto();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }


    // Getters y Setters
    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(String facturaId) {
        this.facturaId = facturaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    @Override
    public String toString() {
        return "VentaDTO{"
                + "idVenta='" + idVenta + '\''
                + ", facturaId='" + facturaId + '\''
                + ", productoId='" + productoId + '\''
                + ", cantidad=" + cantidad
                + ", precioUnitario=" + precioUnitario
                + ", subtotal=" + getSubtotal()
                + ", productoNombre=" + (producto != null ? producto.getNombre() : "N/A")
                + '}';
    }
}
