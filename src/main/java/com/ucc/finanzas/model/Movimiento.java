package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

/**
 * Clase abstracta base de Ingreso y Gasto.
 * Abstracta porque nunca se instancia directamente: siempre es un tipo concreto.
 * @MappedSuperclass hace que sus campos se mapeen en las tablas hijas, no crea tabla propia.
 */
@MappedSuperclass
public abstract class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double monto;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Movimiento() {
    }

    public Movimiento(double monto, Date fecha, String descripcion,
                      Categoria categoria, Usuario usuario) {
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    // Abstracto: Ingreso.registrar() suma al balance, Gasto.registrar() resta (polimorfismo dinamico)
    public abstract void registrar();

    public boolean validarMonto() {
        return this.monto > 0;
    }

    public Long getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setMonto(double monto) { this.monto = monto; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    // JPA necesita estos setters para reasignar categoria y usuario al cargar desde BD
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", monto=" + monto +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "null") +
                ", usuario=" + (usuario != null ? usuario.getUsername() : "null") +
                '}';
    }
}
