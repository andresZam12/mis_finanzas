package com.ucc.finanzas.service;

import com.ucc.finanzas.dto.AhorroRequest;
import com.ucc.finanzas.model.Ahorro;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.AhorroRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Gestiona los registros de ahorro.
 * Los ahorros se generan automaticamente al registrar un ingreso con porcentajeAhorro > 0,
 * o se pueden crear manualmente desde el endpoint /ahorros.
 */
@Service
public class AhorroService {

    private final AhorroRepository ahorroRepository;
    private final AuthService authService;

    public AhorroService(AhorroRepository ahorroRepository, AuthService authService) {
        this.ahorroRepository = ahorroRepository;
        this.authService = authService;
    }

    public Ahorro registrar(AhorroRequest request) {
        Usuario usuario = authService.buscarPorId(request.getUsuarioId());
        Ahorro ahorro = new Ahorro(
                request.getMonto(),
                request.getPorcentaje(),
                request.getDescripcion(),
                new Date(),
                usuario
        );
        return ahorroRepository.save(ahorro);
    }

    // Llamado internamente desde MovimientoService cuando se registra un ingreso con porcentaje
    public Ahorro registrarDesdeIngreso(double montoIngreso, double porcentaje,
                                        String descripcionIngreso, Usuario usuario) {
        double montoAhorro = montoIngreso * porcentaje / 100.0;
        Ahorro ahorro = new Ahorro(
                montoAhorro,
                porcentaje,
                "Ahorro de: " + descripcionIngreso,
                new Date(),
                usuario
        );
        return ahorroRepository.save(ahorro);
    }

    public List<Ahorro> listarPorUsuario(Long usuarioId) {
        return ahorroRepository.findByUsuarioId(usuarioId);
    }

    public double totalAhorros(Long usuarioId) {
        return ahorroRepository.findByUsuarioId(usuarioId)
                .stream()
                .mapToDouble(Ahorro::getMonto)
                .sum();
    }

    public void eliminar(Long id) {
        if (!ahorroRepository.existsById(id)) {
            throw new RuntimeException("Ahorro no encontrado con id: " + id);
        }
        ahorroRepository.deleteById(id);
    }
}
