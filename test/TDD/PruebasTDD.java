package TDD;

import Modelo.Boleta_datos;
import Modelo.Cart;
import Modelo.ProductoDigital;
import Modelo.ProductoFisico;
import Modelo.SeguridadContrasena;
import ModeloDTO.ItemCarritoDTO;
import ModeloDTO.ProductoDTO;

public class PruebasTDD {

    private static int pruebasEjecutadas = 0;
    private static int pruebasCorrectas = 0;

    public static void main(String[] args) {
        System.out.println("===== PRUEBAS TDD DEL PROYECTO INTEGRADOR =====");

        // ---- Carrito (ya existentes) ----
        probarSubtotalDeItemCarrito();
        probarCambioDeCantidadActualizaSubtotal();
        probarCarritoVacioLuegoDeLimpiar();
        probarEliminarIndiceInvalidoNoRompeCarrito();

        // ---- Seguridad (ya existentes) ----
        probarCifradoConsistente();
        probarCifradoDiferenciado();
        probarVerificarRechazaClaveIncorrecta();
        probarVerificarAceptaContrasenaPlanaLegacy();

        // ---- Seguridad (nuevas: casos limite) ----
        probarVerificarRechazaHashNuloOVacio();
        probarHashEsSensibleAMayusculas();

        // ---- ItemCarritoDTO (nuevas: casos limite) ----
        probarSubtotalConCantidadCero();
        probarToStringDeItemCarritoContieneDatosClave();

        // ---- Boleta_datos (nuevas) ----
        probarAgregarProductoCalculaSubtotal();
        probarAgregarVariosProductosAcumulaTotal();

        // ---- Herencia / polimorfismo de Producto (nuevas) ----
        probarTipoProductoDigital();
        probarTipoProductoFisico();
        probarDatosBasicosDeProducto();

        // ---- ProductoDTO (nueva) ----
        probarSettersDeProductoDTOActualizanValores();

        System.out.println("-----------------------------------------------");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Pruebas correctas: " + pruebasCorrectas);

        if (pruebasEjecutadas == pruebasCorrectas) {
            System.out.println("RESULTADO: TODAS LAS PRUEBAS PASARON");
        } else {
            throw new AssertionError("RESULTADO: HAY PRUEBAS FALLIDAS");
        }
    }

    // ============================================================
    // CARRITO (pruebas originales)
    // ============================================================

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

    // ============================================================
    // SEGURIDAD DE CONTRASENA (originales + nuevas)
    // ============================================================

    private static void probarCifradoConsistente() {
        String hash1 = SeguridadContrasena.hash("claveSegura2026");
        String hash2 = SeguridadContrasena.hash("claveSegura2026");

        assertTrue(hash1.equals(hash2), "El hash debe ser identico para la misma contrasena");
    }

    private static void probarCifradoDiferenciado() {
        String hash1 = SeguridadContrasena.hash("claveUno");
        String hash2 = SeguridadContrasena.hash("claveDos");

        assertTrue(!hash1.equals(hash2), "El hash debe diferir para contrasenas distintas");
    }

    private static void probarVerificarRechazaClaveIncorrecta() {
        String hash = SeguridadContrasena.hash("claveCorrecta");

        assertTrue(!SeguridadContrasena.verificar("claveIncorrecta", hash), "La verificacion debe rechazar una clave incorrecta");
    }

    private static void probarVerificarAceptaContrasenaPlanaLegacy() {
        assertTrue(SeguridadContrasena.verificar("clavePlana", "clavePlana"), "La verificacion debe aceptar contrasenas planas de registros antiguos");
    }

    private static void probarVerificarRechazaHashNuloOVacio() {
        assertTrue(!SeguridadContrasena.verificar("cualquierClave", null), "La verificacion debe rechazar cuando la contrasena almacenada es nula");
        assertTrue(!SeguridadContrasena.verificar("cualquierClave", ""), "La verificacion debe rechazar cuando la contrasena almacenada esta vacia");
    }

    private static void probarHashEsSensibleAMayusculas() {
        String hash1 = SeguridadContrasena.hash("Clave123");
        String hash2 = SeguridadContrasena.hash("clave123");

        assertTrue(!hash1.equals(hash2), "El hash debe ser distinto si cambian mayusculas/minusculas");
    }

    // ============================================================
    // ITEM CARRITO DTO (casos limite nuevos)
    // ============================================================

    private static void probarSubtotalConCantidadCero() {
        ProductoDTO producto = new ProductoDTO(3, "Audifonos", 120.00, 8);
        ItemCarritoDTO item = new ItemCarritoDTO(producto, 0);

        assertEquals(0.00, item.getSubtotal(), "El subtotal con cantidad 0 debe ser 0");
    }

    private static void probarToStringDeItemCarritoContieneDatosClave() {
        ProductoDTO producto = new ProductoDTO(4, "Monitor", 500.00, 3);
        ItemCarritoDTO item = new ItemCarritoDTO(producto, 2);

        String texto = item.toString();
        assertTrue(texto.contains("Monitor") && texto.contains("1000.0"),
                "El toString() del item debe incluir el nombre del producto y el subtotal");
    }

    // ============================================================
    // BOLETA_DATOS (nuevas)
    // ============================================================

    private static void probarAgregarProductoCalculaSubtotal() {
        Boleta_datos boleta = new Boleta_datos();
        boleta.setPrecio(25.00);
        boleta.setCantidad(4);

        boleta.agregarProducto();

        assertEquals(100.00, boleta.getSubtotal(), "El subtotal debe ser precio x cantidad al agregar un producto");
    }

    private static void probarAgregarVariosProductosAcumulaTotal() {
        Boleta_datos boleta = new Boleta_datos();

        boleta.setPrecio(10.00);
        boleta.setCantidad(2);
        boleta.agregarProducto(); // subtotal 20

        boleta.setPrecio(15.00);
        boleta.setCantidad(1);
        boleta.agregarProducto(); // subtotal 15, total acumulado 35

        assertEquals(35.00, boleta.getTotal(), "El total de la boleta debe acumularse al agregar varios productos");
    }

    // ============================================================
    // PRODUCTO (herencia / polimorfismo) - nuevas
    // ============================================================

    private static void probarTipoProductoDigital() {
        ProductoDigital producto = new ProductoDigital("D1", "Licencia Antivirus", 45.00);

        assertTrue(producto.tipoProducto().equals("Digital"), "Un ProductoDigital debe reportar tipo 'Digital'");
    }

    private static void probarTipoProductoFisico() {
        ProductoFisico producto = new ProductoFisico("F1", "Mouse Gamer", 50.00);

        assertTrue(producto.tipoProducto().equals("Físico"), "Un ProductoFisico debe reportar tipo 'Físico'");
    }

    private static void probarDatosBasicosDeProducto() {
        ProductoDigital producto = new ProductoDigital("D2", "Curso Online", 99.90);

        assertTrue(producto.getId().equals("D2"), "El id del producto debe conservarse desde el constructor");
        assertEquals(99.90, producto.getPrecio(), "El precio del producto debe conservarse desde el constructor");
    }

    // ============================================================
    // PRODUCTO DTO - nueva
    // ============================================================

    private static void probarSettersDeProductoDTOActualizanValores() {
        ProductoDTO producto = new ProductoDTO();
        producto.setIdProducto(9);
        producto.setNombre("Webcam");
        producto.setPrecio(75.50);
        producto.setStock(12);
        producto.setCategoria("Accesorios");

        assertTrue(producto.getIdProducto() == 9, "El id del producto debe actualizarse con el setter");
        assertTrue(producto.getNombre().equals("Webcam"), "El nombre del producto debe actualizarse con el setter");
        assertEquals(75.50, producto.getPrecio(), "El precio del producto debe actualizarse con el setter");
        assertTrue(producto.getStock() == 12, "El stock del producto debe actualizarse con el setter");
        assertTrue(producto.getCategoria().equals("Accesorios"), "La categoria del producto debe actualizarse con el setter");
    }

    // ============================================================
    // UTILIDADES DE ASERCION
    // ============================================================

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