package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.MetaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de MetaAhorro.
 * Solo declara metodos de consulta; la logica va en MetaAhorroService.
 */
public interface MetaAhorroRepository extends JpaRepository<MetaAhorro, Long> {

    /**
     * Busca la meta de ahorro de un usuario para un mes y anio especificos.
     * Retorna Optional porque puede no existir meta para ese periodo.
     */
    Optional<MetaAhorro> findByUsuarioIdAndMesAndAnio(Long usuarioId, int mes, int anio);

    List<MetaAhorro> findByUsuarioId(Long usuarioId);
}
