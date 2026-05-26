package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Ingreso.
 * Solo declara metodos de consulta; la logica va en MovimientoService.
 */
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    /**
     * Lista todos los ingresos de un usuario especifico.
     */
    List<Ingreso> findByUsuarioId(Long usuarioId);

    /**
     * Lista los ingresos de un usuario filtrados por si son recurrentes o no.
     * Se usa en aplicarRecurrentes() para clonar al siguiente mes.
     */
    List<Ingreso> findByUsuarioIdAndEsRecurrente(Long usuarioId, boolean esRecurrente);
}
