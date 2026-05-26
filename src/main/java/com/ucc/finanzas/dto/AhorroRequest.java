package com.ucc.finanzas.dto;

public class AhorroRequest {

    private double monto;
    private double porcentaje;
    private String descripcion;
    private Long usuarioId;

    public AhorroRequest() {}

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
