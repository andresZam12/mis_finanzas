package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Gasto.
 * Solo declara metodos de consulta; la logica va en MovimientoService.
 */
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    /**
     * Lista todos los gastos de un usuario especifico.
     */
    List<Gasto> findByUsuarioId(Long usuarioId);

    /**
     * Lista los gastos de un usuario filtrados por si son recurrentes o no.
     * Se usa en aplicarRecurrentes() para clonar al siguiente mes.
     */
    List<Gasto> findByUsuarioIdAndEsRecurrente(Long usuarioId, boolean esRecurrente);
}
