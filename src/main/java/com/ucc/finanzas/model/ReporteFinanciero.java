package com.ucc.finanzas.model;

/**
 * Reporte financiero mensual. No es @Entity: se genera en memoria por peticion,
 * no se persiste porque los datos ya estan en las tablas ingreso y gasto.
 */
public class ReporteFinanciero {

    private int mes;
    private int anio;
    private double totalIngresos;
    private double totalGastos;
    private double balance;
    private String nombreUsuario;

    public ReporteFinanciero() {
    }

    public ReporteFinanciero(int mes, int anio, double totalIngresos,
                             double totalGastos, String nombreUsuario) {
        this.mes = mes;
        this.anio = anio;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.nombreUsuario = nombreUsuario;
        this.balance = calcularBalance();
    }

    public double calcularBalance() {
        return this.totalIngresos - this.totalGastos;
    }

    public String generarResumen() {
        return "===== Reporte Financiero =====\n"
                + "Usuario:        " + nombreUsuario + "\n"
                + "Periodo:        " + mes + "/" + anio + "\n"
                + "Total ingresos: $" + totalIngresos + "\n"
                + "Total gastos:   $" + totalGastos + "\n"
                + "Balance:        $" + balance + "\n"
                + "==============================";
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public double getBalance() {
        return balance;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    @Override
    public String toString() {
        return generarResumen();
    }
}
