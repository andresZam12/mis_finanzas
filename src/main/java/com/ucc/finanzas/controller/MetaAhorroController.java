package com.ucc.finanzas.controller;

import com.ucc.finanzas.dto.MetaAhorroRequest;
import com.ucc.finanzas.model.MetaAhorro;
import com.ucc.finanzas.service.MetaAhorroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Endpoints de metas de ahorro. Todos requieren JWT.
 */
@RestController
@RequestMapping("/metas")
public class MetaAhorroController {

    private final MetaAhorroService metaAhorroService;

    public MetaAhorroController(MetaAhorroService metaAhorroService) {
        this.metaAhorroService = metaAhorroService;
    }

    // POST /metas — body: { montoMeta, mes, anio, usuarioId }
    @PostMapping
    public ResponseEntity<?> crearMeta(@RequestBody MetaAhorroRequest request) {
        try {
            MetaAhorro meta = metaAhorroService.crearMeta(request);
            return ResponseEntity.ok(meta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /metas/{usuarioId}/verificar?mes=5&anio=2026
    @GetMapping("/{usuarioId}/verificar")
    public ResponseEntity<?> verificarCumplimiento(@PathVariable Long usuarioId,
                                                   @RequestParam int mes,
                                                   @RequestParam int anio) {
        try {
            boolean cumplio = metaAhorroService.verificarCumplimiento(usuarioId, mes, anio);
            return ResponseEntity.ok(cumplio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /metas/{usuarioId}?mes=5&anio=2026
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obtenerMeta(@PathVariable Long usuarioId,
                                         @RequestParam int mes,
                                         @RequestParam int anio) {
        Optional<MetaAhorro> meta = metaAhorroService.obtenerMetaMes(usuarioId, mes, anio);
        if (meta.isPresent()) {
            return ResponseEntity.ok(meta.get());
        }
        return ResponseEntity.notFound().build();
    }

    // GET /metas/{usuarioId}/todas
    @GetMapping("/{usuarioId}/todas")
    public ResponseEntity<List<MetaAhorro>> listarTodas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(metaAhorroService.listarPorUsuario(usuarioId));
    }

    // DELETE /metas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            metaAhorroService.eliminarMeta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
