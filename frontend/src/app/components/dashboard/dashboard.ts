import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { ReporteService } from '../../services/reporte.service';
import { AhorroService } from '../../services/ahorro.service';
import { MovimientoService } from '../../services/movimiento.service';
import { ReporteFinanciero } from '../../models/reporte.model';

interface TipoPagoRow { tipo: string; ingresos: number; gastos: number; balance: number; }

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  reporte: ReporteFinanciero | null = null;
  balanceTotal: number | null = null;
  totalAhorros: number | null = null;
  tipoPagoRows: TipoPagoRow[] = [];
  cargando = false;
  error = '';

  mesActual = new Date().getMonth() + 1;
  anioActual = new Date().getFullYear();
  mesBusqueda = this.mesActual;
  anioBusqueda = this.anioActual;

  meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

  constructor(
    private authService: AuthService,
    private reporteService: ReporteService,
    private ahorroService: AhorroService,
    private movimientoService: MovimientoService
  ) {}

  ngOnInit() {
    this.cargarBalance();
    this.cargarReporte();
    this.cargarAhorros();
    this.cargarDesgloseTipoPago();
  }

  cargarBalance() {
    const uid = this.authService.getUsuarioId();
    this.reporteService.calcularBalanceTotal(uid).subscribe({
      next: b => this.balanceTotal = b,
      error: () => {}
    });
  }

  cargarAhorros() {
    const uid = this.authService.getUsuarioId();
    this.ahorroService.totalAhorros(uid).subscribe({
      next: t => this.totalAhorros = t,
      error: () => {}
    });
  }

  cargarDesgloseTipoPago() {
    const uid = this.authService.getUsuarioId();
    forkJoin({
      ingresos: this.movimientoService.listarIngresos(uid).pipe(catchError(() => of([]))),
      gastos: this.movimientoService.listarGastos(uid).pipe(catchError(() => of([])))
    }).subscribe({
      next: ({ ingresos, gastos }) => {
        const map = new Map<string, TipoPagoRow>();
        const get = (tipo: string) => {
          if (!map.has(tipo)) map.set(tipo, { tipo, ingresos: 0, gastos: 0, balance: 0 });
          return map.get(tipo)!;
        };
        ingresos.forEach(i => { const r = get(i.tipoPago || 'Sin especificar'); r.ingresos += i.monto; r.balance += i.monto; });
        gastos.forEach(g => { const r = get(g.tipoPago || 'Sin especificar'); r.gastos += g.monto; r.balance -= g.monto; });
        this.tipoPagoRows = Array.from(map.values()).sort((a, b) => a.tipo.localeCompare(b.tipo));
      },
      error: () => {}
    });
  }

  cargarReporte() {
    this.cargando = true;
    this.error = '';
    const uid = this.authService.getUsuarioId();
    this.reporteService.generarReporteMensual(uid, this.mesBusqueda, this.anioBusqueda).subscribe({
      next: r => { this.reporte = r; this.cargando = false; },
      error: err => {
        this.error = err.error || 'No hay datos para ese período';
        this.reporte = null;
        this.cargando = false;
      }
    });
  }

  get nombreMes(): string { return this.meses[(this.mesBusqueda - 1)] || ''; }
}
