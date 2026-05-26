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
 * Representa una deuda del usuario. Tipo: "YO_DEBO" o "ME_DEBEN".
 * El estado nace siempre como "PENDIENTE" y solo cambia a "PAGADA" con marcarPagada().
 */
@Entity
@Table(name = "deuda")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String persona;

    @Column(nullable = false)
    private double monto;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(length = 50)
    private String tipoPago; // Efectivo, Tarjeta, Transferencia, Otro

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, columnDefinition = "double precision default 0")
    private double montoPagado = 0.0;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Deuda() {
    }

    // El estado no se recibe como parametro: siempre nace "PENDIENTE" para evitar inconsistencias
    public Deuda(String persona, double monto, String descripcion,
                 String tipo, String tipoPago, Date fechaRegistro, Usuario usuario) {
        this.persona = persona;
        this.monto = monto;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.tipoPago = tipoPago;
        this.fechaRegistro = fechaRegistro;
        this.usuario = usuario;
        this.estado = "PENDIENTE";
    }

    // Retorna false si ya estaba pagada, evita marcarla dos veces
    public boolean marcarPagada() {
        if (!"PENDIENTE".equals(this.estado)) {
            return false;
        }
        this.montoPagado = this.monto;
        this.estado = "PAGADA";
        return true;
    }

    // Registra un abono parcial; si cubre el total, marca como PAGADA automaticamente
    public void abonar(double monto) {
        this.montoPagado += monto;
        if (this.montoPagado >= this.monto) {
            this.montoPagado = this.monto;
            this.estado = "PAGADA";
        }
    }

    public double getMontoPagado() { return montoPagado; }
    public double getMontoRestante() { return this.monto - this.montoPagado; }

    public boolean validarMonto() {
        return this.monto > 0;
    }

    public boolean validarTipo() {
        if (this.tipo == null) {
            return false;
        }
        String t = this.tipo.trim().toUpperCase();
        return t.equals("YO_DEBO") || t.equals("ME_DEBEN");
    }

    public Long getId() {
        return id;
    }

    public String getPersona() {
        return persona;
    }

    public double getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public void setPersona(String persona) { this.persona = persona; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Deuda{" +
                "id=" + id +
                ", persona='" + persona + '\'' +
                ", monto=" + monto +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
