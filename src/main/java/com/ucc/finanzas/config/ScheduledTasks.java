package com.ucc.finanzas.config;

import com.ucc.finanzas.repository.UsuarioRepository;
import com.ucc.finanzas.service.ReporteDiarioService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final UsuarioRepository usuarioRepository;
    private final ReporteDiarioService reporteDiarioService;

    public ScheduledTasks(UsuarioRepository usuarioRepository,
                          ReporteDiarioService reporteDiarioService) {
        this.usuarioRepository = usuarioRepository;
        this.reporteDiarioService = reporteDiarioService;
    }

    // Genera el snapshot diario de cada usuario a las 23:59 todos los días
    @Scheduled(cron = "0 59 23 * * *")
    public void generarReportesDiarios() {
        usuarioRepository.findAll().forEach(u -> {
            try {
                reporteDiarioService.generarReporteDiario(u.getId());
            } catch (Exception e) {
                System.err.println("Error reporte diario usuario " + u.getId() + ": " + e.getMessage());
            }
        });
    }
}
