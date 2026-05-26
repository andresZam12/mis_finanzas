import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { MovimientoService } from '../../../services/movimiento.service';
import { CategoriaService } from '../../../services/categoria.service';
import { AuthService } from '../../../services/auth.service';
import { Categoria } from '../../../models/categoria.model';

@Component({
  selector: 'app-edit-movimiento',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './edit-movimiento.html'
})
export class EditMovimientoComponent implements OnInit {
  form: FormGroup;
  tipo: 'ingreso' | 'gasto' = 'ingreso';
  categorias: Categoria[] = [];
  error = '';
  cargando = false;
  id!: number;

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
      fuente: [''],
      tipoPago: [''],
      esRecurrente: [false]
    });
  }

  ngOnInit() {
    this.tipo = this.route.snapshot.data['tipo'] || 'ingreso';
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    const tipoCategoria = this.tipo === 'ingreso' ? 'INGRESO' : 'GASTO';

    this.categoriaService.listarPorTipo(tipoCategoria).subscribe({
      next: cats => {
        this.categorias = cats;
        this.cargarDatos();
      }
    });
  }

  get esIngreso() { return this.tipo === 'ingreso'; }

  cargarDatos() {
    const obs = (this.esIngreso
      ? this.movimientoService.obtenerIngreso(this.id)
      : this.movimientoService.obtenerGasto(this.id)) as Observable<any>;

    obs.subscribe({
      next: (m: any) => {
        this.form.patchValue({
          monto: m.monto,
          descripcion: m.descripcion,
          categoriaId: m.categoria?.id,
          fuente: m.fuente || '',
          tipoPago: m.tipoPago || '',
          esRecurrente: m.esRecurrente
        });
      },
      error: () => { this.error = 'No se pudo cargar el registro.'; }
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const val = this.form.value;
    const req = {
      monto: Number(val.monto),
      descripcion: val.descripcion,
      categoriaId: Number(val.categoriaId),
      usuarioId: this.authService.getUsuarioId(),
      fuente: val.fuente || undefined,
      tipoPago: val.tipoPago || undefined,
      esRecurrente: val.esRecurrente
    };

    const obs = (this.esIngreso
      ? this.movimientoService.editarIngreso(this.id, req)
      : this.movimientoService.editarGasto(this.id, req)) as Observable<any>;

    obs.subscribe({
      next: () => this.router.navigate([this.esIngreso ? '/movimientos/ingresos' : '/movimientos/gastos']),
      error: (err: any) => { this.error = err.error || 'Error al guardar'; this.cargando = false; }
    });
  }
}
