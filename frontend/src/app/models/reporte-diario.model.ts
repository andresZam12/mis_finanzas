export interface ReporteDiario {
  id?: number;
  usuarioId: number;
  fecha: string;
  totalIngresos: number;
  totalEgresos: number;
  totalAhorro: number;
  movimientos?: string;
}
