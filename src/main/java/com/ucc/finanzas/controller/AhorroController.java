package com.ucc.finanzas.controller;

import com.ucc.finanzas.dto.AhorroRequest;
import com.ucc.finanzas.model.Ahorro;
import com.ucc.finanzas.service.AhorroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de ahorros. Todos requieren JWT.
 */
@RestController
@RequestMapping("/ahorros")
public class AhorroController {

    private final AhorroService ahorroService;

    public AhorroController(AhorroService ahorroService) {
        this.ahorroService = ahorroService;
    }

    // POST /ahorros — body: { monto, porcentaje, descripcion, usuarioId }
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody AhorroRequest request) {
        try {
            Ahorro ahorro = ahorroService.registrar(request);
            return ResponseEntity.ok(ahorro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /ahorros/{usuarioId}
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Ahorro>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ahorroService.listarPorUsuario(usuarioId));
    }

    // GET /ahorros/{usuarioId}/total
    @GetMapping("/{usuarioId}/total")
    public ResponseEntity<Double> total(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ahorroService.totalAhorros(usuarioId));
    }

    // DELETE /ahorros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            ahorroService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
