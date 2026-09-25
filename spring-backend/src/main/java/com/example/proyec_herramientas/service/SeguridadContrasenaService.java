package com.example.proyec_herramientas.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SeguridadContrasenaService {

    private static final int LONGITUD_SHA256 = 64;
    private static final String PREFIJO_BCRYPT = "$2";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hash(String contrasena) {
        return encoder.encode(contrasena);
    }

    public boolean verificar(String contrasena, String almacenada) {
        if (almacenada == null || almacenada.isEmpty()) {
            return false;
        }
        if (almacenada.startsWith(PREFIJO_BCRYPT)) {
            return encoder.matches(contrasena, almacenada);
        }
        if (almacenada.length() == LONGITUD_SHA256) {
            return sha256Hex(contrasena).equals(almacenada);
        }
        return contrasena.equals(almacenada);
    }

    private String sha256Hex(String contrasena) {
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
}