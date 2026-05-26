package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Usuario.
 * Extiende JpaRepository para heredar las operaciones CRUD basicas.
 * Solo declara consultas personalizadas; la logica va en el Service.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     * Lo usa Spring Security para cargar el usuario al validar el JWT.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si ya existe un usuario con ese username.
     * Lo usa AuthService al registrar para evitar duplicados.
     */
    boolean existsByUsername(String username);
}
