package com.ucc.finanzas.service;

import com.ucc.finanzas.model.Categoria;
import com.ucc.finanzas.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de categorias.
 * Permite crear, editar, eliminar y listar las categorias que clasifican ingresos y gastos.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria crear(String nombre, String tipo) {
        Categoria categoria = new Categoria(nombre, tipo.toUpperCase());

        if (!categoria.validarNombre()) {
            throw new RuntimeException("El nombre de la categoria no puede estar vacio.");
        }
        if (!categoria.validarTipo()) {
            throw new RuntimeException("El tipo de categoria solo puede ser INGRESO o GASTO.");
        }

        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, String nombre, String tipo) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));

        categoria.setNombre(nombre);
        categoria.setTipo(tipo.toUpperCase());

        if (!categoria.validarNombre()) {
            throw new RuntimeException("El nombre de la categoria no puede estar vacio.");
        }
        if (!categoria.validarTipo()) {
            throw new RuntimeException("El tipo de categoria solo puede ser INGRESO o GASTO.");
        }

        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarPorTipo(String tipo) {
        return categoriaRepository.findByTipo(tipo.toUpperCase());
    }
}
