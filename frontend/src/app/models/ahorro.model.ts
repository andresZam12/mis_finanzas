export interface Ahorro {
  id: number;
  monto: number;
  porcentaje: number;
  descripcion: string;
  fecha: string;
  usuario: { id: number; username: string; nombre: string };
}

export interface AhorroRequest {
  monto: number;
  porcentaje: number;
  descripcion: string;
  usuarioId: number;
}
