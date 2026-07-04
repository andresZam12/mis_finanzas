package com.ucc.finanzas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucc.finanzas.model.Ahorro;
import com.ucc.finanzas.model.Gasto;
import com.ucc.finanzas.model.Ingreso;
import com.ucc.finanzas.model.ReporteDiario;
import com.ucc.finanzas.repository.AhorroRepository;
import com.ucc.finanzas.repository.GastoRepository;
import com.ucc.finanzas.repository.IngresoRepository;
import com.ucc.finanzas.repository.ReporteDiarioRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ReporteDiarioService {

    private final ReporteDiarioRepository reporteDiarioRepository;
    private final IngresoRepository ingresoRepository;
    private final GastoRepository gastoRepository;
    private final AhorroRepository ahorroRepository;
    private final ObjectMapper objectMapper;

    public ReporteDiarioService(ReporteDiarioRepository reporteDiarioRepository,
                                IngresoRepository ingresoRepository,
                                GastoRepository gastoRepository,
                                AhorroRepository ahorroRepository,
                                ObjectMapper objectMapper) {
        this.reporteDiarioRepository = reporteDiarioRepository;
        this.ingresoRepository = ingresoRepository;
        this.gastoRepository = gastoRepository;
        this.ahorroRepository = ahorroRepository;
        this.objectMapper = objectMapper;
    }

    public ReporteDiario generarReporteDiario(Long usuarioId) {
        Date hoy = fechaHoy();

        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndFecha(usuarioId, hoy);
        List<Gasto> gastos = gastoRepository.findByUsuarioIdAndFecha(usuarioId, hoy);
        List<Ahorro> ahorros = ahorroRepository.findByUsuarioIdAndFecha(usuarioId, hoy);

        double totalIngresos = ingresos.stream().mapToDouble(Ingreso::getMonto).sum();
        double totalEgresos  = gastos.stream().mapToDouble(Gasto::getMonto).sum();
        double totalAhorro   = ahorros.stream().mapToDouble(Ahorro::getMonto).sum();

        List<Map<String, Object>> items = new ArrayList<>();
        for (Ingreso ing : ingresos) {
            Map<String, Object> item = new HashMap<>();
            item.put("tipo", "ingreso");
            item.put("monto", ing.getMonto());
            item.put("descripcion", ing.getDescripcion() != null ? ing.getDescripcion() : "");
            item.put("fuente", ing.getFuente());
            items.add(item);
        }
        for (Gasto gasto : gastos) {
            Map<String, Object> item = new HashMap<>();
            item.put("tipo", "gasto");
            item.put("monto", gasto.getMonto());
            item.put("descripcion", gasto.getDescripcion() != null ? gasto.getDescripcion() : "");
            items.add(item);
        }

        String movimientosJson;
        try {
            movimientosJson = objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            movimientosJson = "[]";
        }

        Optional<ReporteDiario> existente = reporteDiarioRepository.findByUsuarioIdAndFecha(usuarioId, hoy);
        ReporteDiario reporte;
        if (existente.isPresent()) {
            reporte = existente.get();
            reporte.setTotalIngresos(totalIngresos);
            reporte.setTotalEgresos(totalEgresos);
            reporte.setTotalAhorro(totalAhorro);
            reporte.setMovimientos(movimientosJson);
        } else {
            reporte = new ReporteDiario(usuarioId, hoy, totalIngresos, totalEgresos, totalAhorro, movimientosJson);
        }

        return reporteDiarioRepository.save(reporte);
    }

    public List<ReporteDiario> listarPorMes(Long usuarioId, int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicio = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fin = cal.getTime();

        return reporteDiarioRepository.findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);
    }

    public List<ReporteDiario> calcularMes(Long usuarioId, int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicio = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fin = cal.getTime();

        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);
        List<Gasto> gastos = gastoRepository.findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);
        List<Ahorro> ahorros = ahorroRepository.findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Double> ingresosPorFecha = new HashMap<>();
        Map<String, Double> gastosPorFecha = new HashMap<>();
        Map<String, Double> ahorrosPorFecha = new HashMap<>();
        Map<String, List<Map<String, Object>>> itemsPorFecha = new HashMap<>();

        for (Ingreso ing : ingresos) {
            String key = sdf.format(ing.getFecha());
            ingresosPorFecha.merge(key, ing.getMonto(), Double::sum);
            Map<String, Object> item = new HashMap<>();
            item.put("tipo", "ingreso");
            item.put("monto", ing.getMonto());
            item.put("descripcion", ing.getDescripcion() != null ? ing.getDescripcion() : "");
            item.put("fuente", ing.getFuente() != null ? ing.getFuente() : "");
            itemsPorFecha.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        for (Gasto gasto : gastos) {
            String key = sdf.format(gasto.getFecha());
            gastosPorFecha.merge(key, gasto.getMonto(), Double::sum);
            Map<String, Object> item = new HashMap<>();
            item.put("tipo", "gasto");
            item.put("monto", gasto.getMonto());
            item.put("descripcion", gasto.getDescripcion() != null ? gasto.getDescripcion() : "");
            itemsPorFecha.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        for (Ahorro ahorro : ahorros) {
            String key = sdf.format(ahorro.getFecha());
            ahorrosPorFecha.merge(key, ahorro.getMonto(), Double::sum);
        }

        Set<String> fechas = new HashSet<>();
        fechas.addAll(ingresosPorFecha.keySet());
        fechas.addAll(gastosPorFecha.keySet());
        fechas.addAll(ahorrosPorFecha.keySet());

        List<ReporteDiario> result = new ArrayList<>();
        for (String fechaStr : fechas) {
            try {
                Date fecha = sdf.parse(fechaStr);
                double totalI = ingresosPorFecha.getOrDefault(fechaStr, 0.0);
                double totalE = gastosPorFecha.getOrDefault(fechaStr, 0.0);
                double totalA = ahorrosPorFecha.getOrDefault(fechaStr, 0.0);
                List<Map<String, Object>> items = itemsPorFecha.getOrDefault(fechaStr, new ArrayList<>());
                String movJson;
                try {
                    movJson = objectMapper.writeValueAsString(items);
                } catch (JsonProcessingException e) {
                    movJson = "[]";
                }
                result.add(new ReporteDiario(usuarioId, fecha, totalI, totalE, totalA, movJson));
            } catch (Exception ignored) {
            }
        }

        result.sort(Comparator.comparing(ReporteDiario::getFecha));
        return result;
    }

    private Date fechaHoy() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
