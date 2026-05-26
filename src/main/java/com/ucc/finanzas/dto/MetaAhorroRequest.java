package com.ucc.finanzas.dto;

/**
 * DTO para crear una meta de ahorro mensual.
 * El usuario indica cuanto quiere ahorrar en un mes y anio especificos.
 */
public class MetaAhorroRequest {

    private double montoMeta;
    private int mes;
    private int anio;
    private Long usuarioId;

    public MetaAhorroRequest() {
    }

    public double getMontoMeta() {
        return montoMeta;
    }

    public void setMontoMeta(double montoMeta) {
        this.montoMeta = montoMeta;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
