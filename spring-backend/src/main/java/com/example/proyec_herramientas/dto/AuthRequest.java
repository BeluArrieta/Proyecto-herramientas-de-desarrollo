package com.example.proyec_herramientas.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {

    @NotBlank
    private String usuario;

    @NotBlank
    private String contrasena;

    public AuthRequest() {
    }

    public AuthRequest(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}