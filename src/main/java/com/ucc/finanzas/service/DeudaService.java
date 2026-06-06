package com.ucc.finanzas.service;

import com.ucc.finanzas.dto.DeudaRequest;
import com.ucc.finanzas.model.Categoria;
import com.ucc.finanzas.model.Deuda;
import com.ucc.finanzas.model.Gasto;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.CategoriaRepository;
import com.ucc.finanzas.repository.DeudaRepository;
import com.ucc.finanzas.repository.GastoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Maneja el registro, edicion, pago, eliminacion y consulta de deudas del usuario.
 */
@Service
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuthService authService;

    public DeudaService(DeudaRepository deudaRepository,
                        GastoRepository gastoRepository,
                        CategoriaRepository categoriaRepository,
                        AuthService authService) {
        this.deudaRepository = deudaRepository;
        this.gastoRepository = gastoRepository;
        this.categoriaRepository = categoriaRepository;
        this.authService = authService;
    }

    public Deuda registrarDeuda(DeudaRequest request) {
        Usuario usuario = authService.buscarPorId(request.getUsuarioId());

        Deuda deuda = new Deuda(
                request.getPersona(),
                request.getMonto(),
                request.getDescripcion(),
                request.getTipo().toUpperCase(),
                normalizarTipoPago(request.getTipoPago()),
                new Date(),
                usuario
        );

        if (!deuda.validarMonto()) {
            throw new RuntimeException("El monto de la deuda debe ser mayor a cero.");
        }
        if (!deuda.validarTipo()) {
            throw new RuntimeException("El tipo de deuda solo puede ser YO_DEBO o ME_DEBEN.");
        }

        deuda.setRecurrente(request.isRecurrente());
        return deudaRepository.save(deuda);
    }

    public Deuda editarDeuda(Long id, DeudaRequest request) {
        Deuda deuda = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada con id: " + id));

        deuda.setPersona(request.getPersona());
        deuda.setMonto(request.getMonto());
        deuda.setDescripcion(request.getDescripcion());
        deuda.setTipo(request.getTipo().toUpperCase());
        deuda.setTipoPago(normalizarTipoPago(request.getTipoPago()));

        if (!deuda.validarMonto()) {
            throw new RuntimeException("El monto de la deuda debe ser mayor a cero.");
        }
        if (!deuda.validarTipo()) {
            throw new RuntimeException("El tipo de deuda solo puede ser YO_DEBO o ME_DEBEN.");
        }

        deuda.setRecurrente(request.isRecurrente());
        return deudaRepository.save(deuda);
    }

    public Deuda marcarPagada(Long deudaId) {
        Deuda deuda = deudaRepository.findById(deudaId)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada con id: " + deudaId));

        boolean cambiado = deuda.marcarPagada();
        if (!cambiado) {
            throw new RuntimeException("La deuda ya estaba marcada como PAGADA. No se puede pagar dos veces.");
        }

        deudaRepository.save(deuda);
        if (deuda.isRecurrente()) {
            regenerar(deuda);
        }
        return deuda;
    }

    public Deuda abonarDeuda(Long deudaId, double monto) {
        Deuda deuda = deudaRepository.findById(deudaId)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada con id: " + deudaId));

        if (!"PENDIENTE".equals(deuda.getEstado())) {
            throw new RuntimeException("La deuda ya está pagada.");
        }
        if (monto <= 0) {
            throw new RuntimeException("El monto del abono debe ser mayor a cero.");
        }
        double restante = deuda.getMontoRestante();
        if (monto > restante) {
            throw new RuntimeException("El abono ($" + monto + ") excede el saldo pendiente ($" + restante + ").");
        }

        deuda.abonar(monto);

        List<Categoria> categorias = categoriaRepository.findByTipo("GASTO");
        if (categorias.isEmpty()) {
            throw new RuntimeException("Debe crear al menos una categoría de tipo GASTO para registrar el abono.");
        }
        Gasto gasto = new Gasto(
                monto,
                new Date(),
                "Abono deuda (" + deuda.getPersona() + "): " + deuda.getDescripcion(),
                categorias.get(0),
                deuda.getUsuario(),
                deuda.getTipoPago() != null ? deuda.getTipoPago() : "OTRO",
                false
        );
        gastoRepository.save(gasto);
        deudaRepository.save(deuda);

        if ("PAGADA".equals(deuda.getEstado()) && deuda.isRecurrente()) {
            regenerar(deuda);
        }
        return deuda;
    }

    public void eliminarDeuda(Long id) {
        if (!deudaRepository.existsById(id)) {
            throw new RuntimeException("Deuda no encontrada con id: " + id);
        }
        deudaRepository.deleteById(id);
    }

    public Deuda obtenerPorId(Long id) {
        return deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada con id: " + id));
    }

    public List<Deuda> listarPorUsuario(Long usuarioId) {
        return deudaRepository.findByUsuarioId(usuarioId);
    }

    public List<Deuda> listarPorEstado(Long usuarioId, String estado) {
        return deudaRepository.findByUsuarioIdAndEstado(usuarioId, estado.toUpperCase());
    }

    public List<Deuda> listarPorTipo(Long usuarioId, String tipo) {
        return deudaRepository.findByUsuarioIdAndTipo(usuarioId, tipo.toUpperCase());
    }

    private void regenerar(Deuda origen) {
        Deuda nueva = new Deuda(
                origen.getPersona(),
                origen.getMonto(),
                origen.getDescripcion(),
                origen.getTipo(),
                origen.getTipoPago(),
                new Date(),
                origen.getUsuario()
        );
        nueva.setRecurrente(true);
        deudaRepository.save(nueva);
    }

    private String normalizarTipoPago(String tipoPago) {
        if (tipoPago == null || tipoPago.trim().isEmpty()) return null;
        return tipoPago.trim().toUpperCase();
    }
}
