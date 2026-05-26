import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MovimientoService } from '../../../services/movimiento.service';
import { AuthService } from '../../../services/auth.service';
import { Ingreso } from '../../../models/movimiento.model';

@Component({
  selector: 'app-list-ingresos',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './list-ingresos.html'
})
export class ListIngresosComponent implements OnInit {
  ingresos: Ingreso[] = [];
  cargando = true;
  total = 0;
  eliminandoId: number | null = null;

  constructor(private movimientoService: MovimientoService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.authService.getUsuarioId();
    this.movimientoService.listarIngresos(uid).subscribe({
      next: lista => { this.ingresos = lista; this.total = lista.reduce((s, i) => s + i.monto, 0); this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este ingreso?')) return;
    this.eliminandoId = id;
    this.movimientoService.eliminarIngreso(id).subscribe({
      next: () => { this.ingresos = this.ingresos.filter(i => i.id !== id); this.total = this.ingresos.reduce((s, i) => s + i.monto, 0); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
