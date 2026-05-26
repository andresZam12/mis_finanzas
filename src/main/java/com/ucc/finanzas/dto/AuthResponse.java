package com.ucc.finanzas.dto;

/**
 * DTO para la respuesta de autenticacion exitosa.
 */
public class AuthResponse {

    private String token;
    private String refreshToken;
    private Long usuarioId;
    private String username;

    public AuthResponse() {
    }

    public AuthResponse(String token, String refreshToken, Long usuarioId, String username) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.usuarioId = usuarioId;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsername() {
        return username;
    }
}
