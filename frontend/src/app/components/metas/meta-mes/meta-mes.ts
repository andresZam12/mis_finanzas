import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MetaAhorroService } from '../../../services/meta-ahorro.service';
import { AuthService } from '../../../services/auth.service';
import { MetaAhorro } from '../../../models/meta-ahorro.model';

@Component({
  selector: 'app-meta-mes',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, RouterLink],
  templateUrl: './meta-mes.html'
})
export class MetaMesComponent implements OnInit {
  meta: MetaAhorro | null = null;
  cumplio: boolean | null = null;
  cargando = false;
  error = '';

  mes = new Date().getMonth() + 1;
  anio = new Date().getFullYear();

  meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

  constructor(private metaService: MetaAhorroService, private authService: AuthService) {}

  ngOnInit() { this.buscar(); }

  buscar() {
    this.cargando = true;
    this.error = '';
    this.cumplio = null;
    const uid = this.authService.getUsuarioId();
    this.metaService.obtenerMetaMes(uid, this.mes, this.anio).subscribe({
      next: m => {
        this.meta = m;
        this.verificar();
      },
      error: () => {
        this.meta = null;
        this.cargando = false;
        this.error = 'No hay meta para ' + this.meses[this.mes - 1] + ' ' + this.anio;
      }
    });
  }

  verificar() {
    const uid = this.authService.getUsuarioId();
    this.metaService.verificarCumplimiento(uid, this.mes, this.anio).subscribe({
      next: r => { this.cumplio = r; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get nombreMes(): string { return this.meses[this.mes - 1] || ''; }
}
