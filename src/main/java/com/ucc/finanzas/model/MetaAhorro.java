package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Meta de ahorro mensual del usuario. Ejemplo: "quiero ahorrar $500.000 en mayo 2026".
 * mes y anio son int porque la meta es mensual, no diaria.
 */
@Entity
@Table(name = "meta_ahorro")
public class MetaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double montoMeta;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int anio;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public MetaAhorro() {
    }

    public MetaAhorro(double montoMeta, int mes, int anio, Usuario usuario) {
        this.montoMeta = montoMeta;
        this.mes = mes;
        this.anio = anio;
        this.usuario = usuario;
    }

    // Retorna true si el usuario ahorro igual o mas que la meta en ese mes
    public boolean verificarCumplimiento(double balanceDelMes) {
        return balanceDelMes >= this.montoMeta;
    }

    public boolean validarMonto() {
        return this.montoMeta > 0;
    }

    public Long getId() {
        return id;
    }

    public double getMontoMeta() {
        return montoMeta;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setMontoMeta(double montoMeta) {
        this.montoMeta = montoMeta;
    }

    @Override
    public String toString() {
        return "MetaAhorro{" +
                "id=" + id +
                ", montoMeta=" + montoMeta +
                ", mes=" + mes +
                ", anio=" + anio +
                ", usuario=" + (usuario != null ? usuario.getUsername() : "null") +
                '}';
    }
}
