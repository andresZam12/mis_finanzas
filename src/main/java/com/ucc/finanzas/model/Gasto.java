package com.ucc.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Representa dinero que sale del sistema (arriendo, comida, servicios, etc.).
 * Extiende Movimiento. Su registrar() resta del balance (vs Ingreso que suma).
 */
@Entity
@Table(name = "gasto")
public class Gasto extends Movimiento implements Recurrente {

    @Column(length = 50)
    private String tipoPago;

    @Column(nullable = false)
    private boolean esRecurrente;

    public Gasto() {
        super();
    }

    public Gasto(double monto, Date fecha, String descripcion,
                 Categoria categoria, Usuario usuario,
                 String tipoPago, boolean esRecurrente) {
        super(monto, fecha, descripcion, categoria, usuario);
        this.tipoPago = tipoPago;
        this.esRecurrente = esRecurrente;
    }

    // Polimorfismo: esta version de registrar() resta del balance del usuario
    @Override
    public void registrar() {
        System.out.println("Gasto registrado con pago en '" + this.tipoPago
                + "' por valor de $" + this.getMonto()
                + ". Resta del balance del usuario.");
    }

    @Override
    public void aplicarRecurrencia() {
        if (this.esRecurrente) {
            System.out.println("El gasto recurrente con pago en '" + this.tipoPago
                    + "' se aplicara nuevamente el proximo mes.");
        } else {
            System.out.println("Este gasto NO es recurrente. No se aplicara.");
        }
    }

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
        return "Gasto{" +
                "id=" + getId() +
                ", monto=" + getMonto() +
                ", fecha=" + getFecha() +
                ", tipoPago='" + tipoPago + '\'' +
                ", esRecurrente=" + esRecurrente +
                ", descripcion='" + getDescripcion() + '\'' +
                ", categoria=" + (getCategoria() != null ? getCategoria().getNombre() : "null") +
                '}';
    }
}
