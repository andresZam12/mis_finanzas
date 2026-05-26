package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Deuda.
 * Solo declara metodos de consulta; la logica va en DeudaService.
 */
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    /**
     * Lista todas las deudas de un usuario.
     */
    List<Deuda> findByUsuarioId(Long usuarioId);

    /**
     * Lista las deudas de un usuario filtradas por estado: PENDIENTE o PAGADA.
     */
    List<Deuda> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    /**
     * Lista las deudas de un usuario filtradas por tipo: YO_DEBO o ME_DEBEN.
     */
    List<Deuda> findByUsuarioIdAndTipo(Long usuarioId, String tipo);
}
