package com.ucc.finanzas.controller;

import com.ucc.finanzas.model.Categoria;
import com.ucc.finanzas.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de categorias. Todos requieren JWT.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // POST /categorias?nombre=Barberia&tipo=INGRESO
    @PostMapping
    public ResponseEntity<?> crear(@RequestParam String nombre, @RequestParam String tipo) {
        try {
            return ResponseEntity.ok(categoriaService.crear(nombre, tipo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /categorias/{id}?nombre=X&tipo=Y
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestParam String nombre,
                                        @RequestParam String tipo) {
        try {
            return ResponseEntity.ok(categoriaService.actualizar(id, nombre, tipo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            categoriaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoriaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // GET /categorias/tipo?tipo=INGRESO
    @GetMapping("/tipo")
    public ResponseEntity<List<Categoria>> listarPorTipo(@RequestParam String tipo) {
        return ResponseEntity.ok(categoriaService.listarPorTipo(tipo));
    }
}
