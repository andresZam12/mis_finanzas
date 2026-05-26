export interface Deuda {
  id: number;
  persona: string;
  monto: number;
  montoPagado: number;
  montoRestante: number;
  descripcion: string;
  tipo: string; // YO_DEBO | ME_DEBEN
  tipoPago?: string;
  estado: string; // PENDIENTE | PAGADA
  fechaRegistro: string;
  usuario: { id: number; username: string; nombre: string };
}

export interface DeudaRequest {
  persona: string;
  monto: number;
  descripcion: string;
  tipo: string;
  tipoPago?: string;
  usuarioId: number;
}
