package com.ucc.finanzas.controller;

import com.ucc.finanzas.dto.MovimientoRequest;
import com.ucc.finanzas.model.Gasto;
import com.ucc.finanzas.model.Ingreso;
import com.ucc.finanzas.service.MovimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de ingresos, gastos y balance. Todos requieren JWT.
 */
@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    // POST /movimientos/ingreso
    @PostMapping("/ingreso")
    public ResponseEntity<?> registrarIngreso(@RequestBody MovimientoRequest request) {
        try {
            return ResponseEntity.ok(movimientoService.registrarIngreso(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /movimientos/gasto
    @PostMapping("/gasto")
    public ResponseEntity<?> registrarGasto(@RequestBody MovimientoRequest request) {
        try {
            return ResponseEntity.ok(movimientoService.registrarGasto(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /movimientos/ingresos/{usuarioId}
    @GetMapping("/ingresos/{id}")
    public ResponseEntity<List<Ingreso>> listarIngresos(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.listarIngresos(id));
    }

    // GET /movimientos/gastos/{usuarioId}
    @GetMapping("/gastos/{id}")
    public ResponseEntity<List<Gasto>> listarGastos(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.listarGastos(id));
    }

    // GET /movimientos/ingreso/{id}
    @GetMapping("/ingreso/{id}")
    public ResponseEntity<?> obtenerIngreso(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(movimientoService.obtenerIngresoPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /movimientos/gasto/{id}
    @GetMapping("/gasto/{id}")
    public ResponseEntity<?> obtenerGasto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(movimientoService.obtenerGastoPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /movimientos/ingreso/{id}
    @PutMapping("/ingreso/{id}")
    public ResponseEntity<?> editarIngreso(@PathVariable Long id, @RequestBody MovimientoRequest request) {
        try {
            return ResponseEntity.ok(movimientoService.editarIngreso(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /movimientos/gasto/{id}
    @PutMapping("/gasto/{id}")
    public ResponseEntity<?> editarGasto(@PathVariable Long id, @RequestBody MovimientoRequest request) {
        try {
            return ResponseEntity.ok(movimientoService.editarGasto(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /movimientos/ingreso/{id}
    @DeleteMapping("/ingreso/{id}")
    public ResponseEntity<?> eliminarIngreso(@PathVariable Long id) {
        try {
            movimientoService.eliminarIngreso(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /movimientos/gasto/{id}
    @DeleteMapping("/gasto/{id}")
    public ResponseEntity<?> eliminarGasto(@PathVariable Long id) {
        try {
            movimientoService.eliminarGasto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /movimientos/balance/{usuarioId}
    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> calcularBalance(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.calcularBalance(id));
    }
}
