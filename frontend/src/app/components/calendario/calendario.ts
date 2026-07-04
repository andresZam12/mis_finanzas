import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DecimalPipe, NgClass } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { ReporteDiarioService } from '../../services/reporte-diario.service';
import { ReporteDiario } from '../../models/reporte-diario.model';

interface MovimientoItem {
  tipo: 'ingreso' | 'gasto';
  monto: number;
  descripcion: string;
  fuente?: string;
}

interface DiaCalendario {
  fecha: Date;
  dia: number;
  esDelMes: boolean;
  esHoy: boolean;
  reporte?: ReporteDiario;
}

@Component({
  selector: 'app-calendario',
  standalone: true,
  imports: [CurrencyPipe, DecimalPipe, NgClass],
  templateUrl: './calendario.html',
  styleUrl: './calendario.css'
})
export class CalendarioComponent implements OnInit {
  anio = new Date().getFullYear();
  mes = new Date().getMonth() + 1;
  dias: DiaCalendario[] = [];
  diaSeleccionado: DiaCalendario | null = null;
  movimientosDetalle: MovimientoItem[] = [];
  cargando = false;
  generando = false;
  error = '';
  exito = '';

  readonly nombresMeses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                           'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
  readonly nombresDias = ['Dom','Lun','Mar','Mié','Jue','Vie','Sáb'];

  constructor(
    private authService: AuthService,
    private reporteDiarioService: ReporteDiarioService
  ) {}

  ngOnInit() { this.cargarMes(); }

  cargarMes() {
    this.cargando = true;
    this.error = '';
    this.diaSeleccionado = null;
    const uid = this.authService.getUsuarioId();
    this.reporteDiarioService.calcularPorMes(uid, this.anio, this.mes).subscribe({
      next: reportes => { this.construirCalendario(reportes); this.cargando = false; },
      error: () => { this.construirCalendario([]); this.cargando = false; }
    });
  }

  construirCalendario(reportes: ReporteDiario[]) {
    const hoy = new Date();
    const map = new Map<string, ReporteDiario>();
    reportes.forEach(r => map.set(r.fecha.substring(0, 10), r));

    const primerDia = new Date(this.anio, this.mes - 1, 1);
    const ultimoDia = new Date(this.anio, this.mes, 0);
    const diasPrevios = primerDia.getDay();

    this.dias = [];

    for (let i = 0; i < diasPrevios; i++) {
      const d = new Date(primerDia);
      d.setDate(d.getDate() - (diasPrevios - i));
      this.dias.push({ fecha: d, dia: d.getDate(), esDelMes: false, esHoy: false });
    }

    for (let d = 1; d <= ultimoDia.getDate(); d++) {
      const fecha = new Date(this.anio, this.mes - 1, d);
      const key = this.formatDate(fecha);
      this.dias.push({
        fecha,
        dia: d,
        esDelMes: true,
        esHoy: fecha.toDateString() === hoy.toDateString(),
        reporte: map.get(key)
      });
    }

    const resto = this.dias.length % 7;
    if (resto !== 0) {
      for (let i = 1; i <= 7 - resto; i++) {
        const d = new Date(ultimoDia);
        d.setDate(d.getDate() + i);
        this.dias.push({ fecha: d, dia: d.getDate(), esDelMes: false, esHoy: false });
      }
    }
  }

  seleccionarDia(dia: DiaCalendario) {
    if (!dia.esDelMes) return;
    this.diaSeleccionado = dia;
    if (dia.reporte?.movimientos) {
      try { this.movimientosDetalle = JSON.parse(dia.reporte.movimientos); }
      catch { this.movimientosDetalle = []; }
    } else {
      this.movimientosDetalle = [];
    }
  }

  cerrarDetalle() { this.diaSeleccionado = null; }

  generarHoy() {
    this.generando = true;
    this.exito = '';
    this.error = '';
    const uid = this.authService.getUsuarioId();
    this.reporteDiarioService.generarHoy(uid).subscribe({
      next: () => {
        this.generando = false;
        this.exito = 'Reporte de hoy generado correctamente.';
        this.cargarMes();
      },
      error: () => {
        this.generando = false;
        this.error = 'Error al generar el reporte. Intente de nuevo.';
      }
    });
  }

  mesAnterior() {
    if (this.mes === 1) { this.mes = 12; this.anio--; }
    else this.mes--;
    this.cargarMes();
  }

  mesSiguiente() {
    if (this.mes === 12) { this.mes = 1; this.anio++; }
    else this.mes++;
    this.cargarMes();
  }

  balance(r: ReporteDiario): number { return r.totalIngresos - r.totalEgresos; }

  get tituloMes(): string { return `${this.nombresMeses[this.mes - 1]} ${this.anio}`; }

  private formatDate(d: Date): string {
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${d.getFullYear()}-${mm}-${dd}`;
  }
}
