package com.ucc.finanzas.controller;

import com.ucc.finanzas.dto.DeudaRequest;
import com.ucc.finanzas.model.Deuda;
import com.ucc.finanzas.service.DeudaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de deudas. Todos requieren JWT.
 */
@RestController
@RequestMapping("/deudas")
public class DeudaController {

    private final DeudaService deudaService;

    public DeudaController(DeudaService deudaService) {
        this.deudaService = deudaService;
    }

    // POST /deudas
    @PostMapping
    public ResponseEntity<?> registrarDeuda(@RequestBody DeudaRequest request) {
        try {
            return ResponseEntity.ok(deudaService.registrarDeuda(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /deudas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> editarDeuda(@PathVariable Long id, @RequestBody DeudaRequest request) {
        try {
            return ResponseEntity.ok(deudaService.editarDeuda(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /deudas/{id}/pagar?tipoPago=TRANSFERENCIA
    @PutMapping("/{id}/pagar")
    public ResponseEntity<?> marcarPagada(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "TRANSFERENCIA") String tipoPago) {
        try {
            return ResponseEntity.ok(deudaService.marcarPagada(id, tipoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /deudas/{id}/abonar?monto=500&tipoPago=EFECTIVO
    @PostMapping("/{id}/abonar")
    public ResponseEntity<?> abonar(@PathVariable Long id,
            @RequestParam double monto,
            @RequestParam(required = false, defaultValue = "TRANSFERENCIA") String tipoPago) {
        try {
            return ResponseEntity.ok(deudaService.abonarDeuda(id, monto, tipoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /deudas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            deudaService.eliminarDeuda(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /deudas/{usuarioId}
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Deuda>> listarDeudas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(deudaService.listarPorUsuario(usuarioId));
    }

    // GET /deudas/{usuarioId}/estado?estado=PENDIENTE
    @GetMapping("/{usuarioId}/estado")
    public ResponseEntity<List<Deuda>> listarPorEstado(@PathVariable Long usuarioId,
                                                       @RequestParam String estado) {
        return ResponseEntity.ok(deudaService.listarPorEstado(usuarioId, estado));
    }

    // GET /deudas/{usuarioId}/tipo?tipo=YO_DEBO
    @GetMapping("/{usuarioId}/tipo")
    public ResponseEntity<List<Deuda>> listarPorTipo(@PathVariable Long usuarioId,
                                                     @RequestParam String tipo) {
        return ResponseEntity.ok(deudaService.listarPorTipo(usuarioId, tipo));
    }
}
