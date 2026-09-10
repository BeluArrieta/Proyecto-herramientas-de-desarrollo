package Modelo;

import ModeloDTO.ProductoDTO;
import ModeloDTO.ItemCarritoDTO;
import ModeloDAO.ProductoDAO;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int idProducto;
    private String nombre;
    private int cantidad;
    private double precio;
    private double subtotal;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    private static List<ItemCarritoDTO> items = new ArrayList<>();
    private static double total = 0.0;

    // Agregar un producto al carrito con validación de stock en BD
    public static void addProducto(ProductoDTO producto, int cantidad) {

        if (producto == null || cantidad <= 0) {
            System.err.println("❌ Producto nulo o cantidad inválida.");
            return;
        }

        ProductoDAO productoDAO = new ProductoDAO();
        ProductoDTO productoDB = productoDAO.buscarPorId(producto.getIdProducto());

        if (productoDB == null) {
            System.err.println("❌ Producto no existe en BD.");
            return;
        }

        int cantidadEnCarrito = 0;

        for (ItemCarritoDTO item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                cantidadEnCarrito = item.getCantidad();
                break;
            }
        }

        int cantidadTotal = cantidadEnCarrito + cantidad;

        if (cantidadTotal > productoDB.getStock()) {
            System.err.println("❌ Stock insuficiente");
            return;
        }

        boolean encontrado = false;

        for (ItemCarritoDTO item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                item.setCantidad(item.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            items.add(new ItemCarritoDTO(producto, cantidad));
        }

        recalcularTotal();
    }

    public static void removeProducto(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            recalcularTotal();
        } else {
            System.err.println("Indice fuera de rango al intentar eliminar del carrito.");
        }
    }

    public static void removeProductoById(int idProducto) {
        items.removeIf(item -> item.getProducto().getIdProducto() == idProducto);
        recalcularTotal();
    }

    public static List<ItemCarritoDTO> getItems() {
        return new ArrayList<>(items); // Protege la lista original
    }

    private static void recalcularTotal() {
        total = 0.0;
        for (ItemCarritoDTO item : items) {
            total += item.getSubtotal();
        }
    }

    public static double getTotal() {
        return total;
    }

    public static void clear() {
        items.clear();
        total = 0.0;
    }

    public static boolean isEmpty() {
        return items.isEmpty();
    }
}
