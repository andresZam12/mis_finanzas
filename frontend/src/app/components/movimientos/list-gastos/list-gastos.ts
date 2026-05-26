import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MovimientoService } from '../../../services/movimiento.service';
import { AuthService } from '../../../services/auth.service';
import { Gasto } from '../../../models/movimiento.model';

@Component({
  selector: 'app-list-gastos',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './list-gastos.html'
})
export class ListGastosComponent implements OnInit {
  gastos: Gasto[] = [];
  cargando = true;
  total = 0;
  eliminandoId: number | null = null;

  constructor(private movimientoService: MovimientoService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.authService.getUsuarioId();
    this.movimientoService.listarGastos(uid).subscribe({
      next: lista => { this.gastos = lista; this.total = lista.reduce((s, g) => s + g.monto, 0); this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este gasto?')) return;
    this.eliminandoId = id;
    this.movimientoService.eliminarGasto(id).subscribe({
      next: () => { this.gastos = this.gastos.filter(g => g.id !== id); this.total = this.gastos.reduce((s, g) => s + g.monto, 0); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
