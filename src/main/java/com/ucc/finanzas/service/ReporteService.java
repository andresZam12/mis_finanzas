package com.ucc.finanzas.service;

import com.ucc.finanzas.model.Ahorro;
import com.ucc.finanzas.model.Gasto;
import com.ucc.finanzas.model.Ingreso;
import com.ucc.finanzas.model.ReporteFinanciero;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.AhorroRepository;
import com.ucc.finanzas.repository.GastoRepository;
import com.ucc.finanzas.repository.IngresoRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Genera el reporte financiero mensual en memoria a partir de los datos de ingreso y gasto.
 */
@Service
public class ReporteService {

    private final IngresoRepository ingresoRepository;
    private final GastoRepository gastoRepository;
    private final AhorroRepository ahorroRepository;
    private final AuthService authService;

    public ReporteService(IngresoRepository ingresoRepository,
                          GastoRepository gastoRepository,
                          AhorroRepository ahorroRepository,
                          AuthService authService) {
        this.ingresoRepository = ingresoRepository;
        this.gastoRepository = gastoRepository;
        this.ahorroRepository = ahorroRepository;
        this.authService = authService;
    }

    public ReporteFinanciero generarReporteMensual(Long usuarioId, int mes, int anio) {
        Usuario usuario = authService.buscarPorId(usuarioId);
        Calendar cal = Calendar.getInstance();

        List<Ingreso> todosIngresos = ingresoRepository.findByUsuarioId(usuarioId);
        double totalIngresos = todosIngresos.stream()
                .filter(i -> {
                    cal.setTime(i.getFecha());
                    // Calendar.MONTH es base 0 (enero=0), por eso se suma 1
                    return (cal.get(Calendar.MONTH) + 1) == mes
                            && cal.get(Calendar.YEAR) == anio;
                })
                .mapToDouble(Ingreso::getMonto)
                .sum();

        List<Gasto> todosGastos = gastoRepository.findByUsuarioId(usuarioId);
        double totalGastos = todosGastos.stream()
                .filter(g -> {
                    cal.setTime(g.getFecha());
                    return (cal.get(Calendar.MONTH) + 1) == mes
                            && cal.get(Calendar.YEAR) == anio;
                })
                .mapToDouble(Gasto::getMonto)
                .sum();

        cal.set(anio, mes - 1, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date iniciomes = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date finMes = cal.getTime();

        double totalAhorros = ahorroRepository
                .findByUsuarioIdAndFechaBetween(usuarioId, iniciomes, finMes)
                .stream().mapToDouble(Ahorro::getMonto).sum();

        return new ReporteFinanciero(mes, anio, totalIngresos, totalGastos, totalAhorros, usuario.getNombre());
    }

    public double calcularBalanceTotal(Long usuarioId) {
        double totalIngresos = ingresoRepository.findByUsuarioId(usuarioId)
                .stream()
                .mapToDouble(Ingreso::getMonto)
                .sum();

        double totalGastos = gastoRepository.findByUsuarioId(usuarioId)
                .stream()
                .mapToDouble(Gasto::getMonto)
                .sum();

        return totalIngresos - totalGastos;
    }
}
