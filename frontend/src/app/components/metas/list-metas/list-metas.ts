import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MetaAhorroService } from '../../../services/meta-ahorro.service';
import { AuthService } from '../../../services/auth.service';
import { MetaAhorro } from '../../../models/meta-ahorro.model';

const MESES = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

@Component({
  selector: 'app-list-metas',
  standalone: true,
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './list-metas.html'
})
export class ListMetasComponent implements OnInit {
  metas: MetaAhorro[] = [];
  cargando = true;
  eliminandoId: number | null = null;

  constructor(
    private metaService: MetaAhorroService,
    private authService: AuthService
  ) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando = true;
    this.metaService.listarPorUsuario(this.authService.getUsuarioId()).subscribe({
      next: m => { this.metas = m; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  nombreMes(mes: number): string { return MESES[mes - 1] || String(mes); }

  eliminar(id: number) {
    if (!confirm('¿Eliminar esta meta?')) return;
    this.eliminandoId = id;
    this.metaService.eliminar(id).subscribe({
      next: () => { this.metas = this.metas.filter(m => m.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
