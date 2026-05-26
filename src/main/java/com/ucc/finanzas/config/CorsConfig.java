package com.ucc.finanzas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuracion CORS del sistema.
 *
 * Permite que el frontend Angular (localhost:4200) pueda hacer peticiones
 * HTTP al backend sin que el navegador las bloquee por politica de mismo origen.
 *
 * Se define como un bean independiente para poder inyectarlo tanto en
 * SecurityConfig como en la capa MVC.
 */
@Configuration
public class CorsConfig {

    /**
     * Define las reglas CORS aceptadas por el servidor.
     * Solo se permite el origen de Angular en desarrollo.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // En desarrollo acepta cualquier origen (Postman, Angular, etc.)
        config.setAllowedOriginPatterns(List.of("*"));

        // Metodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Todos los headers permitidos (incluye Authorization para JWT)
        config.setAllowedHeaders(List.of("*"));

        // Permite enviar cookies y headers de autorizacion
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
