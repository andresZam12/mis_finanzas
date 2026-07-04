package com.ucc.finanzas.controller;

import com.ucc.finanzas.model.ReporteDiario;
import com.ucc.finanzas.service.ReporteDiarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportes-diarios")
public class ReporteDiarioController {

    private final ReporteDiarioService reporteDiarioService;

    public ReporteDiarioController(ReporteDiarioService reporteDiarioService) {
        this.reporteDiarioService = reporteDiarioService;
    }

    @GetMapping("/{usuarioId}/mes")
    public ResponseEntity<List<ReporteDiario>> listarPorMes(
            @PathVariable Long usuarioId,
            @RequestParam int anio,
            @RequestParam int mes) {
        return ResponseEntity.ok(reporteDiarioService.listarPorMes(usuarioId, anio, mes));
    }

    @GetMapping("/{usuarioId}/mes-calculado")
    public ResponseEntity<List<ReporteDiario>> calcularMes(
            @PathVariable Long usuarioId,
            @RequestParam int anio,
            @RequestParam int mes) {
        return ResponseEntity.ok(reporteDiarioService.calcularMes(usuarioId, anio, mes));
    }

    @PostMapping("/generar/{usuarioId}")
    public ResponseEntity<?> generar(@PathVariable Long usuarioId) {
        try {
            ReporteDiario reporte = reporteDiarioService.generarReporteDiario(usuarioId);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
