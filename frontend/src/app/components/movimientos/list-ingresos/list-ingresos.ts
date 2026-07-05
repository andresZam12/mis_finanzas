import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MovimientoService } from '../../../services/movimiento.service';
import { AuthService } from '../../../services/auth.service';
import { Ingreso } from '../../../models/movimiento.model';

@Component({
  selector: 'app-list-ingresos',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './list-ingresos.html'
})
export class ListIngresosComponent implements OnInit {
  ingresos: Ingreso[] = [];
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
    this.movimientoService.listarIngresos(uid).subscribe({
      next: lista => { this.ingresos = lista; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get ingresosDelMes(): Ingreso[] {
    const mm = String(this.mesFiltro).padStart(2, '0');
    const prefix = `${this.anioFiltro}-${mm}`;
    return this.ingresos.filter(i => i.fecha?.startsWith(prefix));
  }

  get totalDelMes(): number {
    return this.ingresosDelMes.reduce((s, i) => s + i.monto, 0);
  }

  get nombreMesFiltro(): string { return this.meses[this.mesFiltro - 1]; }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este ingreso?')) return;
    this.eliminandoId = id;
    this.movimientoService.eliminarIngreso(id).subscribe({
      next: () => { this.ingresos = this.ingresos.filter(i => i.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
