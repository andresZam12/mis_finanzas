package com.ucc.finanzas.dto;

/**
 * DTO para registrar un ingreso o un gasto.
 *
 * Campos comunes a ambos tipos:
 *   monto, descripcion, categoriaId, usuarioId, esRecurrente.
 *
 * Campos exclusivos de Ingreso: fuente.
 * Campos exclusivos de Gasto:   tipoPago.
 *
 * El controller determina que tipo crear segun el endpoint que se llame:
 *   POST /movimientos/ingreso -> usa fuente
 *   POST /movimientos/gasto  -> usa tipoPago
 */
public class MovimientoRequest {

    private double monto;
    private String descripcion;
    private Long categoriaId;
    private Long usuarioId;
    private String fuente;
    private String tipoPago;
    private boolean esRecurrente;
    private double porcentajeAhorro; // 0-100, solo aplica en ingresos

    public MovimientoRequest() {
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public boolean isEsRecurrente() {
        return esRecurrente;
    }

    public void setEsRecurrente(boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    public double getPorcentajeAhorro() { return porcentajeAhorro; }
    public void setPorcentajeAhorro(double porcentajeAhorro) { this.porcentajeAhorro = porcentajeAhorro; }
}
