package com.ucc.finanzas.service;

import com.ucc.finanzas.dto.MovimientoRequest;
import com.ucc.finanzas.model.Categoria;
import com.ucc.finanzas.model.Gasto;
import com.ucc.finanzas.model.Ingreso;
import com.ucc.finanzas.model.Usuario;
import com.ucc.finanzas.repository.CategoriaRepository;
import com.ucc.finanzas.repository.GastoRepository;
import com.ucc.finanzas.repository.IngresoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Gestiona el registro, edicion, eliminacion y consulta de ingresos y gastos.
 * El polimorfismo actua aqui: registrar() se llama igual en Ingreso y Gasto,
 * pero Java decide en tiempo de ejecucion cual implementacion ejecutar.
 */
@Service
public class MovimientoService {

    private final IngresoRepository ingresoRepository;
    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuthService authService;
    private final AhorroService ahorroService;

    public MovimientoService(IngresoRepository ingresoRepository,
                             GastoRepository gastoRepository,
                             CategoriaRepository categoriaRepository,
                             AuthService authService,
                             AhorroService ahorroService) {
        this.ingresoRepository = ingresoRepository;
        this.gastoRepository = gastoRepository;
        this.categoriaRepository = categoriaRepository;
        this.authService = authService;
        this.ahorroService = ahorroService;
    }

    public Ingreso registrarIngreso(MovimientoRequest request) {
        Usuario usuario = authService.buscarPorId(request.getUsuarioId());
        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Ingreso ingreso = new Ingreso(
                request.getMonto(),
                new Date(),
                request.getDescripcion(),
                categoria,
                usuario,
                request.getFuente(),
                normalizarTipoPago(request.getTipoPago()),
                request.isEsRecurrente()
        );

        if (!ingreso.validarMonto()) {
            throw new RuntimeException("El monto del ingreso debe ser mayor a cero.");
        }

        ingreso.registrar();
        Ingreso guardado = ingresoRepository.save(ingreso);

        // Si se especifica porcentaje de ahorro, crear un registro de ahorro automaticamente
        if (request.getPorcentajeAhorro() > 0) {
            ahorroService.registrarDesdeIngreso(
                    guardado.getMonto(),
                    request.getPorcentajeAhorro(),
                    guardado.getDescripcion(),
                    usuario
            );
        }

        return guardado;
    }

    public Gasto registrarGasto(MovimientoRequest request) {
        Usuario usuario = authService.buscarPorId(request.getUsuarioId());
        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Gasto gasto = new Gasto(
                request.getMonto(),
                new Date(),
                request.getDescripcion(),
                categoria,
                usuario,
                normalizarTipoPago(request.getTipoPago()),
                request.isEsRecurrente()
        );

        if (!gasto.validarMonto()) {
            throw new RuntimeException("El monto del gasto debe ser mayor a cero.");
        }

        gasto.registrar();
        return gastoRepository.save(gasto);
    }

    public Ingreso editarIngreso(Long id, MovimientoRequest request) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con id: " + id));
        Categoria categoria = buscarCategoria(request.getCategoriaId());

        ingreso.setMonto(request.getMonto());
        ingreso.setDescripcion(request.getDescripcion());
        ingreso.setCategoria(categoria);
        ingreso.setFuente(request.getFuente());
        ingreso.setTipoPago(normalizarTipoPago(request.getTipoPago()));
        ingreso.setEsRecurrente(request.isEsRecurrente());

        if (!ingreso.validarMonto()) {
            throw new RuntimeException("El monto del ingreso debe ser mayor a cero.");
        }

        return ingresoRepository.save(ingreso);
    }

    public Gasto editarGasto(Long id, MovimientoRequest request) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + id));
        Categoria categoria = buscarCategoria(request.getCategoriaId());

        gasto.setMonto(request.getMonto());
        gasto.setDescripcion(request.getDescripcion());
        gasto.setCategoria(categoria);
        gasto.setTipoPago(normalizarTipoPago(request.getTipoPago()));
        gasto.setEsRecurrente(request.isEsRecurrente());

        if (!gasto.validarMonto()) {
            throw new RuntimeException("El monto del gasto debe ser mayor a cero.");
        }

        return gastoRepository.save(gasto);
    }

    public void eliminarIngreso(Long id) {
        if (!ingresoRepository.existsById(id)) {
            throw new RuntimeException("Ingreso no encontrado con id: " + id);
        }
        ingresoRepository.deleteById(id);
    }

    public void eliminarGasto(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new RuntimeException("Gasto no encontrado con id: " + id);
        }
        gastoRepository.deleteById(id);
    }

    public Ingreso obtenerIngresoPorId(Long id) {
        return ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con id: " + id));
    }

    public Gasto obtenerGastoPorId(Long id) {
        return gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + id));
    }

    public List<Ingreso> listarIngresos(Long usuarioId) {
        return ingresoRepository.findByUsuarioId(usuarioId);
    }

    public List<Gasto> listarGastos(Long usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId);
    }

    public double totalIngresos(Long usuarioId) {
        return ingresoRepository.findByUsuarioId(usuarioId)
                .stream().mapToDouble(Ingreso::getMonto).sum();
    }

    public double totalGastos(Long usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId)
                .stream().mapToDouble(Gasto::getMonto).sum();
    }

    public double calcularBalance(Long usuarioId) {
        return totalIngresos(usuarioId) - totalGastos(usuarioId);
    }

    public void aplicarRecurrentes(Long usuarioId) {
        ingresoRepository.findByUsuarioIdAndEsRecurrente(usuarioId, true)
                .forEach(Ingreso::aplicarRecurrencia);
        gastoRepository.findByUsuarioIdAndEsRecurrente(usuarioId, true)
                .forEach(Gasto::aplicarRecurrencia);
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + categoriaId));
    }

    private String normalizarTipoPago(String tipoPago) {
        if (tipoPago == null || tipoPago.trim().isEmpty()) return null;
        return tipoPago.trim().toUpperCase();
    }
}
