package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Categoria.
 * Solo declara metodos de consulta; la logica va en CategoriaService.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Lista todas las categorias de un tipo especifico: INGRESO o GASTO.
     */
    List<Categoria> findByTipo(String tipo);
}
