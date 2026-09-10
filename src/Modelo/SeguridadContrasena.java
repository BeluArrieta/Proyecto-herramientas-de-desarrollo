package Modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SeguridadContrasena {

    private static final int LONGITUD_HASH = 64;

    private SeguridadContrasena() {
    }

    public static String hash(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la contrasena", e);
        }
    }

    public static boolean verificar(String contrasena, String almacenada) {
        if (almacenada == null || almacenada.isEmpty()) {
            return false;
        }
        if (almacenada.length() == LONGITUD_HASH) {
            return hash(contrasena).equals(almacenada);
        }
        return contrasena.equals(almacenada);
    }
}