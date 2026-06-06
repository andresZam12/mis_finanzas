package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import java.util.Date;

/**
 * Snapshot diario del estado financiero de un usuario.
 * Se genera al cerrar el dia (manual o automaticamente a las 23:59).
 * La columna movimientos almacena un JSON con el listado de movimientos del dia.
 */
@Entity
@Table(
    name = "reporte_diario",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "fecha"})
)
public class ReporteDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private double totalIngresos;

    @Column(nullable = false)
    private double totalEgresos;

    @Column(nullable = false)
    private double totalAhorro;

    // JSON serializado con la lista de movimientos del dia
    @Column(columnDefinition = "TEXT")
    private String movimientos;

    public ReporteDiario() {}

    public ReporteDiario(Long usuarioId, Date fecha, double totalIngresos,
                         double totalEgresos, double totalAhorro, String movimientos) {
        this.usuarioId     = usuarioId;
        this.fecha         = fecha;
        this.totalIngresos = totalIngresos;
        this.totalEgresos  = totalEgresos;
        this.totalAhorro   = totalAhorro;
        this.movimientos   = movimientos;
    }

    public Long getId() { return id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(double totalIngresos) { this.totalIngresos = totalIngresos; }

    public double getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(double totalEgresos) { this.totalEgresos = totalEgresos; }

    public double getTotalAhorro() { return totalAhorro; }
    public void setTotalAhorro(double totalAhorro) { this.totalAhorro = totalAhorro; }

    public String getMovimientos() { return movimientos; }
    public void setMovimientos(String movimientos) { this.movimientos = movimientos; }

    @Override
    public String toString() {
        return "ReporteDiario{id=" + id + ", usuarioId=" + usuarioId
                + ", fecha=" + fecha + ", totalIngresos=" + totalIngresos
                + ", totalEgresos=" + totalEgresos + ", totalAhorro=" + totalAhorro + "}";
    }
}
