package Modelo;

public class Moneda {

    private Moneda() {
    }

    public static String formatear(double valor) {
        return String.format("S/ %.2f", valor);
    }
}