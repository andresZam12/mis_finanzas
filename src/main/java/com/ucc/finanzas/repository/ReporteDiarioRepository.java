package com.ucc.finanzas.repository;

import com.ucc.finanzas.model.ReporteDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReporteDiarioRepository extends JpaRepository<ReporteDiario, Long> {

    Optional<ReporteDiario> findByUsuarioIdAndFecha(Long usuarioId, Date fecha);

    List<ReporteDiario> findByUsuarioIdAndFechaBetween(Long usuarioId, Date inicio, Date fin);
}
