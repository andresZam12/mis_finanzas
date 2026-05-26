export interface Ingreso {
  id: number;
  monto: number;
  fecha: string;
  descripcion: string;
  fuente: string;
  tipoPago?: string;
  esRecurrente: boolean;
  categoria?: { id: number; nombre: string; tipo: string };
  usuario: { id: number; username: string; nombre: string };
}

export interface Gasto {
  id: number;
  monto: number;
  fecha: string;
  descripcion: string;
  tipoPago: string;
  esRecurrente: boolean;
  categoria?: { id: number; nombre: string; tipo: string };
  usuario: { id: number; username: string; nombre: string };
}

export interface MovimientoRequest {
  monto: number;
  descripcion: string;
  categoriaId: number;
  usuarioId: number;
  fuente?: string;
  tipoPago?: string;
  esRecurrente: boolean;
  porcentajeAhorro?: number;
}
