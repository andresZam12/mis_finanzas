export interface MetaAhorro {
  id?: number;
  montoMeta: number;
  mes: number;
  anio: number;
  usuario?: { id: number; username: string };
}

export interface MetaAhorroRequest {
  montoMeta: number;
  mes: number;
  anio: number;
  usuarioId: number;
}
