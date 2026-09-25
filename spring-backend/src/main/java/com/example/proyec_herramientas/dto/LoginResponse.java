package com.example.proyec_herramientas.dto;

public class LoginResponse {

    private String usuario;
    private String tipoUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;

    public LoginResponse() {
    }

    public LoginResponse(String usuario, String tipoUsuario, String nombre,
            String apellido, String correo, String telefono) {
        this.usuario = usuario;
        this.tipoUsuario = tipoUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}