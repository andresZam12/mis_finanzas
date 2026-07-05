import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AhorroService } from '../../../services/ahorro.service';
import { AuthService } from '../../../services/auth.service';
import { Ahorro } from '../../../models/ahorro.model';

@Component({
  selector: 'app-list-ahorros',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink],
  templateUrl: './list-ahorros.html'
})
export class ListAhorrosComponent implements OnInit {
  ahorros: Ahorro[] = [];
  cargando = true;
  eliminandoId: number | null = null;

  mesFiltro = new Date().getMonth() + 1;
  anioFiltro = new Date().getFullYear();
  readonly meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                    'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

  constructor(private ahorroService: AhorroService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando = true;
    const uid = this.authService.getUsuarioId();
    this.ahorroService.listarPorUsuario(uid).subscribe({
      next: lista => { this.ahorros = lista; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get ahorrosDelMes(): Ahorro[] {
    const mm = String(this.mesFiltro).padStart(2, '0');
    const prefix = `${this.anioFiltro}-${mm}`;
    return this.ahorros.filter(a => a.fecha?.startsWith(prefix));
  }

  get totalDelMes(): number {
    return this.ahorrosDelMes.reduce((s, a) => s + a.monto, 0);
  }

  get nombreMesFiltro(): string { return this.meses[this.mesFiltro - 1]; }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este registro de ahorro?')) return;
    this.eliminandoId = id;
    this.ahorroService.eliminar(id).subscribe({
      next: () => { this.ahorros = this.ahorros.filter(a => a.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
