package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Clasifica los movimientos. El tipo solo puede ser "INGRESO" o "GASTO".
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String tipo;

    public Categoria() {
    }

    public Categoria(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public boolean validarNombre() {
        if (this.nombre == null) {
            return false;
        }
        return !this.nombre.trim().isEmpty();
    }

    public boolean validarTipo() {
        if (this.tipo == null) {
            return false;
        }
        String t = this.tipo.trim().toUpperCase();
        return t.equals("INGRESO") || t.equals("GASTO");
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
