package com.ucc.finanzas.controller;

import com.ucc.finanzas.model.ReporteFinanciero;
import com.ucc.finanzas.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de reportes financieros. Todos requieren JWT.
 * El reporte se genera en memoria, no se persiste en BD.
 */
@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // GET /reportes/{usuarioId}?mes=5&anio=2026
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> generarReporte(@PathVariable Long usuarioId,
                                            @RequestParam int mes,
                                            @RequestParam int anio) {
        try {
            ReporteFinanciero reporte = reporteService.generarReporteMensual(usuarioId, mes, anio);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /reportes/{usuarioId}/balance
    @GetMapping("/{usuarioId}/balance")
    public ResponseEntity<Double> calcularBalanceTotal(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reporteService.calcularBalanceTotal(usuarioId));
    }
}
