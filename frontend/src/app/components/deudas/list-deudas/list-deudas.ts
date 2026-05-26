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

  constructor(private deudaService: DeudaService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.authService.getUsuarioId();
    this.deudaService.listarPorUsuario(uid).subscribe({
      next: d => { this.deudas = d; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  marcarPagada(id: number) {
    this.marcandoId = id;
    this.deudaService.marcarPagada(id).subscribe({
      next: deuda => {
        const idx = this.deudas.findIndex(d => d.id === id);
        if (idx !== -1) this.deudas[idx] = deuda;
        this.marcandoId = null;
      },
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
    if (!monto || monto <= 0) {
      this.abonarError = 'Ingresa un monto mayor a cero.';
      return;
    }
    this.abonando = true;
    this.abonarError = '';
    this.deudaService.abonar(id, monto).subscribe({
      next: deuda => {
        const idx = this.deudas.findIndex(d => d.id === id);
        if (idx !== -1) this.deudas[idx] = deuda;
        this.abonarId = null;
        this.montoAbono = null;
        this.abonando = false;
      },
      error: (err: any) => {
        this.abonarError = err.error || 'Error al registrar el abono.';
        this.abonando = false;
      }
    });
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
