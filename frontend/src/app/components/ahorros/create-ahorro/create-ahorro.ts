import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AhorroService } from '../../../services/ahorro.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-create-ahorro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-ahorro.html'
})
export class CreateAhorroComponent {
  form: FormGroup;
  error = '';
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private ahorroService: AhorroService,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      monto: ['', [Validators.required, Validators.min(1)]],
      porcentaje: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      descripcion: ['', Validators.required]
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const val = this.form.value;
    this.ahorroService.registrar({
      monto: Number(val.monto),
      porcentaje: Number(val.porcentaje),
      descripcion: val.descripcion,
      usuarioId: this.authService.getUsuarioId()
    }).subscribe({
      next: () => this.router.navigate(['/ahorros']),
      error: err => { this.error = err.error || 'Error al registrar'; this.cargando = false; }
    });
  }
}
