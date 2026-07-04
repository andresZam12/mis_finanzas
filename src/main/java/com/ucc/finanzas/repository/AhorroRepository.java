package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.Ahorro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AhorroRepository extends JpaRepository<Ahorro, Long> {
    List<Ahorro> findByUsuarioId(Long usuarioId);
    List<Ahorro> findByUsuarioIdAndFecha(Long usuarioId, Date fecha);

    List<Ahorro> findByUsuarioIdAndFechaBetween(Long usuarioId, Date inicio, Date fin);
}
