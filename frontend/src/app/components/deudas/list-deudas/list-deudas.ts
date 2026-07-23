import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DeudaService } from '../../../services/deuda.service';
import { AuthService } from '../../../services/auth.service';
import { Deuda } from '../../../models/deuda.model';

@Component({
  selector: 'app-list-deudas',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './list-deudas.html'
})
export class ListDeudasComponent implements OnInit {
  deudas: Deuda[] = [];
  cargando = true;
  marcandoId: number | null = null;
  eliminandoId: number | null = null;
  abonarId: number | null = null;
  montoAbono: number | null = null;
  abonarError = '';
  abonando = false;

  mostrarHistorial = false;
  mesFiltroHistorial = new Date().getMonth() + 1;
  anioFiltroHistorial = new Date().getFullYear();
  readonly meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                    'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

  constructor(private deudaService: DeudaService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.authService.getUsuarioId();
    this.deudaService.listarPorUsuario(uid).subscribe({
      next: d => { this.deudas = d; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get deudasPendientes(): Deuda[] {
    return this.deudas.filter(d => d.estado === 'PENDIENTE');
  }

  get deudasPagadasDelMes(): Deuda[] {
    const mm = String(this.mesFiltroHistorial).padStart(2, '0');
    const prefix = `${this.anioFiltroHistorial}-${mm}`;
    return this.deudas
      .filter(d => d.estado === 'PAGADA' && d.fechaRegistro?.startsWith(prefix))
      .sort((a, b) => b.fechaRegistro.localeCompare(a.fechaRegistro));
  }

  get nombreMesHistorial(): string { return this.meses[this.mesFiltroHistorial - 1]; }

  get totalPendienteYoDebo(): number {
    return this.deudasPendientes
      .filter(d => d.tipo === 'YO_DEBO' && !this.esFuturo(d))
      .reduce((s, d) => s + d.montoRestante, 0);
  }

  get totalPendienteMeDeben(): number {
    return this.deudasPendientes
      .filter(d => d.tipo === 'ME_DEBEN' && !this.esFuturo(d))
      .reduce((s, d) => s + d.montoRestante, 0);
  }

  marcarPagada(id: number) {
    this.marcandoId = id;
    this.deudaService.marcarPagada(id).subscribe({
      next: () => { this.marcandoId = null; this.cargar(); },
      error: () => { this.marcandoId = null; }
    });
  }

  iniciarAbono(id: number) {
    this.abonarId = id;
    this.montoAbono = null;
    this.abonarError = '';
  }

  cancelarAbono() {
    this.abonarId = null;
    this.montoAbono = null;
    this.abonarError = '';
  }

  confirmarAbono(id: number) {
    const monto = this.montoAbono;
    if (!monto || monto <= 0) { this.abonarError = 'Ingresa un monto mayor a cero.'; return; }
    this.abonando = true;
    this.abonarError = '';
    this.deudaService.abonar(id, monto).subscribe({
      next: () => { this.abonarId = null; this.montoAbono = null; this.abonando = false; this.cargar(); },
      error: (err: any) => { this.abonarError = err.error || 'Error al registrar el abono.'; this.abonando = false; }
    });
  }

  // Devuelve true si la deuda recurrente aún no corresponde al mes en curso
  esFuturo(d: Deuda): boolean {
    if (!d.recurrente || !d.fechaRegistro) return false;
    const hoy = new Date();
    const mesActual = `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, '0')}`;
    return d.fechaRegistro.substring(0, 7) > mesActual;
  }

  mesDisponible(d: Deuda): string {
    if (!d.fechaRegistro) return '';
    // Añadir mediodía para evitar desfase de zona horaria al parsear solo fecha
    const fecha = new Date(d.fechaRegistro + 'T12:00:00');
    return fecha.toLocaleString('es-CO', { month: 'long', year: 'numeric' });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar esta deuda?')) return;
    this.eliminandoId = id;
    this.deudaService.eliminar(id).subscribe({
      next: () => { this.deudas = this.deudas.filter(d => d.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
