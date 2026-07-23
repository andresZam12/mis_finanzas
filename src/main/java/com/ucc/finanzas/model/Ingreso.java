package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Representa dinero que entra al sistema (salario, freelance, etc.).
 * Extiende Movimiento e implementa Recurrente para ingresos fijos mensuales.
 */
@Entity
@Table(name = "ingreso")
public class Ingreso extends Movimiento implements Recurrente {

    @Column(nullable = true, length = 100)
    private String fuente;

    @Column(length = 50)
    private String tipoPago; // Efectivo, Tarjeta, Transferencia, Otro

    @Column(nullable = false)
    private boolean esRecurrente;

    public Ingreso() {
        super();
    }

    public Ingreso(double monto, Date fecha, String descripcion,
                   Categoria categoria, Usuario usuario,
                   String fuente, String tipoPago, boolean esRecurrente) {
        super(monto, fecha, descripcion, categoria, usuario);
        this.fuente = fuente;
        this.tipoPago = tipoPago;
        this.esRecurrente = esRecurrente;
    }

    // Polimorfismo: esta version de registrar() suma al balance del usuario
    @Override
    public void registrar() {
        System.out.println("Ingreso registrado desde '" + this.fuente
                + "' por valor de $" + this.getMonto()
                + ". Suma al balance del usuario.");
    }

    @Override
    public void aplicarRecurrencia() {
        if (this.esRecurrente) {
            System.out.println("El ingreso recurrente de '" + this.fuente
                    + "' se aplicara nuevamente el proximo mes.");
        } else {
            System.out.println("Este ingreso NO es recurrente. No se aplicara.");
        }
    }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public boolean isEsRecurrente() {
        return esRecurrente;
    }

    public void setEsRecurrente(boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    @Override
    public String toString() {
        return "Ingreso{" +
                "id=" + getId() +
                ", monto=" + getMonto() +
                ", fecha=" + getFecha() +
                ", fuente='" + fuente + '\'' +
                ", esRecurrente=" + esRecurrente +
                ", descripcion='" + getDescripcion() + '\'' +
                ", categoria=" + (getCategoria() != null ? getCategoria().getNombre() : "null") +
                '}';
    }
}
