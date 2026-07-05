import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { MovimientoService } from '../../../services/movimiento.service';
import { CategoriaService } from '../../../services/categoria.service';
import { AuthService } from '../../../services/auth.service';
import { Categoria } from '../../../models/categoria.model';

@Component({
  selector: 'app-create-movimiento',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-movimiento.html'
})
export class CreateMovimientoComponent implements OnInit {
  form: FormGroup;
  tipo: 'ingreso' | 'gasto' = 'ingreso';
  categorias: Categoria[] = [];
  error = '';
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private movimientoService: MovimientoService,
    private categoriaService: CategoriaService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      monto: ['', [Validators.required, Validators.min(1)]],
      descripcion: ['', Validators.required],
      categoriaId: ['', Validators.required],
      tipoPago: [''],
      esRecurrente: [false],
      porcentajeAhorro: [0, [Validators.min(0), Validators.max(100)]]
    });
  }

  ngOnInit() {
    this.tipo = this.route.snapshot.data['tipo'] || 'ingreso';
    const tipoCategoria = this.tipo === 'ingreso' ? 'INGRESO' : 'GASTO';
    this.categoriaService.listarPorTipo(tipoCategoria).subscribe({
      next: cats => this.categorias = cats
    });
  }

  get esIngreso() { return this.tipo === 'ingreso'; }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const uid = this.authService.getUsuarioId();
    const val = this.form.value;
    const req = {
      monto: Number(val.monto),
      descripcion: val.descripcion,
      categoriaId: Number(val.categoriaId),
      usuarioId: uid,
      tipoPago: val.tipoPago || undefined,
      esRecurrente: val.esRecurrente,
      porcentajeAhorro: this.esIngreso ? Number(val.porcentajeAhorro) : 0
    };

    const obs: Observable<any> = this.esIngreso
      ? this.movimientoService.registrarIngreso(req)
      : this.movimientoService.registrarGasto(req);

    obs.subscribe({
      next: () => this.router.navigate([this.esIngreso ? '/movimientos/ingresos' : '/movimientos/gastos']),
      error: (err: any) => {
        this.error = err.error || 'Error al registrar';
        this.cargando = false;
      }
    });
  }
}
