package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

/**
 * Registra el ahorro generado al asignar un porcentaje de un ingreso.
 * Cada vez que se registra un ingreso con porcentajeAhorro > 0,
 * el sistema crea un Ahorro con el monto calculado automaticamente.
 */
@Entity
@Table(name = "ahorro")
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private double porcentaje;

    @Column(length = 255)
    private String descripcion;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Ahorro() {}

    public Ahorro(double monto, double porcentaje, String descripcion, Date fecha, Usuario usuario) {
        this.monto = monto;
        this.porcentaje = porcentaje;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public Long getId() { return id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Ahorro{id=" + id + ", monto=" + monto + ", porcentaje=" + porcentaje
                + ", descripcion='" + descripcion + "', fecha=" + fecha + "}";
    }
}
