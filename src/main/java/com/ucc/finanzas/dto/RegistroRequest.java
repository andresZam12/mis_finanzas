package com.ucc.finanzas.dto;

/**
 * DTO para el registro de un nuevo usuario.
 * Transporta los datos necesarios para crear la cuenta desde el cliente.
 */
public class RegistroRequest {

    private String username;
    private String password;
    private String nombre;

    public RegistroRequest() {
    }

    public RegistroRequest(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
