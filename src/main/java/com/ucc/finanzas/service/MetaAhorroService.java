package com.ucc.finanzas.service;

import com.ucc.finanzas.dto.MetaAhorroRequest;
import com.ucc.finanzas.model.MetaAhorro;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.MetaAhorroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Permite crear metas de ahorro mensuales y verificar si se cumplieron.
 */
@Service
public class MetaAhorroService {

    private final MetaAhorroRepository metaAhorroRepository;
    private final AuthService authService;
    private final ReporteService reporteService;

    public MetaAhorroService(MetaAhorroRepository metaAhorroRepository,
                             AuthService authService,
                             ReporteService reporteService) {
        this.metaAhorroRepository = metaAhorroRepository;
        this.authService = authService;
        this.reporteService = reporteService;
    }

    public MetaAhorro crearMeta(MetaAhorroRequest request) {
        Usuario usuario = authService.buscarPorId(request.getUsuarioId());

        MetaAhorro meta = new MetaAhorro(
                request.getMontoMeta(),
                request.getMes(),
                request.getAnio(),
                usuario
        );

        if (!meta.validarMonto()) {
            throw new RuntimeException("El monto de la meta debe ser mayor a cero.");
        }

        return metaAhorroRepository.save(meta);
    }

    public boolean verificarCumplimiento(Long usuarioId, int mes, int anio) {
        MetaAhorro meta = metaAhorroRepository
                .findByUsuarioIdAndMesAndAnio(usuarioId, mes, anio)
                .orElseThrow(() -> new RuntimeException(
                        "No hay meta registrada para el mes " + mes + "/" + anio));

        double balance = reporteService.generarReporteMensual(usuarioId, mes, anio).getBalance();
        return meta.verificarCumplimiento(balance);
    }

    public Optional<MetaAhorro> obtenerMetaMes(Long usuarioId, int mes, int anio) {
        return metaAhorroRepository.findByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
    }

    public List<MetaAhorro> listarPorUsuario(Long usuarioId) {
        return metaAhorroRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarMeta(Long id) {
        if (!metaAhorroRepository.existsById(id)) {
            throw new RuntimeException("Meta no encontrada con id: " + id);
        }
        metaAhorroRepository.deleteById(id);
    }
}
