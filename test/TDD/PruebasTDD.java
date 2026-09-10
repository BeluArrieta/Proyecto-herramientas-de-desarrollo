package TDD;

import Modelo.Cart;
import ModeloDTO.ItemCarritoDTO;
import ModeloDTO.ProductoDTO;

public class PruebasTDD {

    private static int pruebasEjecutadas = 0;
    private static int pruebasCorrectas = 0;

    public static void main(String[] args) {
        System.out.println("===== PRUEBAS TDD DEL PROYECTO INTEGRADOR =====");

        probarSubtotalDeItemCarrito();
        probarCambioDeCantidadActualizaSubtotal();
        probarCarritoVacioLuegoDeLimpiar();
        probarEliminarIndiceInvalidoNoRompeCarrito();

        System.out.println("-----------------------------------------------");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Pruebas correctas: " + pruebasCorrectas);

        if (pruebasEjecutadas == pruebasCorrectas) {
            System.out.println("RESULTADO: TODAS LAS PRUEBAS PASARON");
        } else {
            throw new AssertionError("RESULTADO: HAY PRUEBAS FALLIDAS");
        }
    }

    private static void probarSubtotalDeItemCarrito() {
        ProductoDTO producto = new ProductoDTO(1, "Mouse Gamer", 50.00, 10);
        ItemCarritoDTO item = new ItemCarritoDTO(producto, 2);

        assertEquals(100.00, item.getSubtotal(), "El subtotal debe ser precio x cantidad");
    }

    private static void probarCambioDeCantidadActualizaSubtotal() {
        ProductoDTO producto = new ProductoDTO(2, "Teclado", 80.00, 5);
        ItemCarritoDTO item = new ItemCarritoDTO(producto, 1);
        item.setCantidad(3);

        assertEquals(240.00, item.getSubtotal(), "Al cambiar la cantidad, el subtotal debe actualizarse");
    }

    private static void probarCarritoVacioLuegoDeLimpiar() {
        Cart.clear();

        assertTrue(Cart.isEmpty(), "El carrito debe quedar vacio despues de usar clear()");
        assertEquals(0.00, Cart.getTotal(), "El total debe quedar en 0 despues de limpiar el carrito");
    }

    private static void probarEliminarIndiceInvalidoNoRompeCarrito() {
    Cart.clear();
    Cart.removeProducto(5);

    assertTrue(Cart.isEmpty(), "Eliminar un indice invalido no debe agregar ni danar el carrito");
    }

    private static void assertEquals(double esperado, double obtenido, String mensaje) {
        pruebasEjecutadas++;
        double margen = 0.001;
        if (Math.abs(esperado - obtenido) <= margen) {
            pruebasCorrectas++;
            System.out.println("OK: " + mensaje);
        } else {
            System.out.println("ERROR: " + mensaje + " | esperado: " + esperado + " | obtenido: " + obtenido);
        }
    }

    private static void assertTrue(boolean condicion, String mensaje) {
        pruebasEjecutadas++;
        if (condicion) {
            pruebasCorrectas++;
            System.out.println("OK: " + mensaje);
        } else {
            System.out.println("ERROR: " + mensaje);
        }
    }
}
