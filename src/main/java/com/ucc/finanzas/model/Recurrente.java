package com.ucc.finanzas.model;

/**
 * Contrato para movimientos que se repiten mensualmente.
 * Se modela como interfaz para que Ingreso y Gasto puedan implementarla
 * ademas de extender Movimiento (Java no permite herencia multiple de clases).
 */
public interface Recurrente {

    void aplicarRecurrencia();
}
