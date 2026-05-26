export interface Menu {
  id: number;
  nombre: string;
  url?: string;
  icono?: string;
  orden: number;
  hijos: Menu[];
}
