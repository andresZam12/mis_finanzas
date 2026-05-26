package com.ucc.finanzas.service;

import com.ucc.finanzas.dto.AuthRequest;
import com.ucc.finanzas.dto.AuthResponse;
import com.ucc.finanzas.dto.RegistroRequest;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.UsuarioRepository;
import com.ucc.finanzas.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Maneja el registro, la autenticacion y la renovacion de tokens.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Usuario registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya esta en uso: " + request.getUsername());
        }

        Usuario usuario = new Usuario(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getNombre()
        );

        if (!usuario.validarUsername()) {
            throw new RuntimeException("El nombre de usuario no es valido: no puede tener espacios ni estar vacio.");
        }

        return usuarioRepository.save(usuario);
    }

    public AuthResponse autenticar(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getUsername()));

        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new RuntimeException("La cuenta esta desactivada. Contacte al administrador.");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta.");
        }

        String token = jwtUtil.generateToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());
        return new AuthResponse(token, refreshToken, usuario.getId(), usuario.getUsername());
    }

    public AuthResponse renovarToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("El refresh token no es valido o ya expiro.");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String nuevoToken = jwtUtil.generateToken(username);
        String nuevoRefreshToken = jwtUtil.generateRefreshToken(username);
        return new AuthResponse(nuevoToken, nuevoRefreshToken, null, username);
    }

    // Lo usan otros services para validar que el usuario exista antes de crear movimientos, deudas, etc.
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }
}
