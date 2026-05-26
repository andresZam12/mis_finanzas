import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AhorroService } from '../../../services/ahorro.service';
import { AuthService } from '../../../services/auth.service';
import { Ahorro } from '../../../models/ahorro.model';

@Component({
  selector: 'app-list-ahorros',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './list-ahorros.html'
})
export class ListAhorrosComponent implements OnInit {
  ahorros: Ahorro[] = [];
  total = 0;
  cargando = true;
  eliminandoId: number | null = null;

  constructor(private ahorroService: AhorroService, private authService: AuthService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando = true;
    const uid = this.authService.getUsuarioId();
    this.ahorroService.listarPorUsuario(uid).subscribe({
      next: lista => {
        this.ahorros = lista;
        this.total = lista.reduce((s, a) => s + a.monto, 0);
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este registro de ahorro?')) return;
    this.eliminandoId = id;
    this.ahorroService.eliminar(id).subscribe({
      next: () => { this.ahorros = this.ahorros.filter(a => a.id !== id); this.total = this.ahorros.reduce((s, a) => s + a.monto, 0); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
