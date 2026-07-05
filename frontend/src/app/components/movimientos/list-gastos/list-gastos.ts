import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MovimientoService } from '../../../services/movimiento.service';
import { AuthService } from '../../../services/auth.service';
import { Gasto } from '../../../models/movimiento.model';

@Component({
  selector: 'app-list-gastos',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './list-gastos.html'
})
export class ListGastosComponent implements OnInit {
  gastos: Gasto[] = [];
  cargando = true;
  eliminandoId: number | null = null;

  mesFiltro = new Date().getMonth() + 1;
  anioFiltro = new Date().getFullYear();
  readonly meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                    'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

  constructor(private movimientoService: MovimientoService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.authService.getUsuarioId();
    this.movimientoService.listarGastos(uid).subscribe({
      next: lista => { this.gastos = lista; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get gastosDelMes(): Gasto[] {
    const mm = String(this.mesFiltro).padStart(2, '0');
    const prefix = `${this.anioFiltro}-${mm}`;
    return this.gastos.filter(g => g.fecha?.startsWith(prefix));
  }

  get totalDelMes(): number {
    return this.gastosDelMes.reduce((s, g) => s + g.monto, 0);
  }

  get nombreMesFiltro(): string { return this.meses[this.mesFiltro - 1]; }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este gasto?')) return;
    this.eliminandoId = id;
    this.movimientoService.eliminarGasto(id).subscribe({
      next: () => { this.gastos = this.gastos.filter(g => g.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
