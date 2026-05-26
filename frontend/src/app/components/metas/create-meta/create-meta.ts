import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MetaAhorroService } from '../../../services/meta-ahorro.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-create-meta',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-meta.html'
})
export class CreateMetaComponent {
  form: FormGroup;
  error = '';
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private metaService: MetaAhorroService,
    private authService: AuthService,
    private router: Router
  ) {
    const hoy = new Date();
    this.form = this.fb.group({
      montoMeta: ['', [Validators.required, Validators.min(1)]],
      mes: [hoy.getMonth() + 1, [Validators.required, Validators.min(1), Validators.max(12)]],
      anio: [hoy.getFullYear(), [Validators.required, Validators.min(2020)]]
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const val = this.form.value;
    this.metaService.crearMeta({
      montoMeta: Number(val.montoMeta),
      mes: Number(val.mes),
      anio: Number(val.anio),
      usuarioId: this.authService.getUsuarioId()
    }).subscribe({
      next: () => this.router.navigate(['/metas']),
      error: err => {
        this.error = err.error || 'Error al crear meta';
        this.cargando = false;
      }
    });
  }
}
