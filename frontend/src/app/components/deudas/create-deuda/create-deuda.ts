import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DeudaService } from '../../../services/deuda.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-create-deuda',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-deuda.html'
})
export class CreateDeudaComponent {
  form: FormGroup;
  error = '';
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private deudaService: DeudaService,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      persona: ['', Validators.required],
      monto: ['', [Validators.required, Validators.min(1)]],
      descripcion: ['', Validators.required],
      tipo: ['YO_DEBO', Validators.required],
      tipoPago: [''],
      recurrente: [false]
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const val = this.form.value;
    this.deudaService.registrar({
      persona: val.persona,
      monto: Number(val.monto),
      descripcion: val.descripcion,
      tipo: val.tipo,
      tipoPago: val.tipoPago || undefined,
      recurrente: val.recurrente,
      usuarioId: this.authService.getUsuarioId()
    }).subscribe({
      next: () => this.router.navigate(['/deudas']),
      error: err => {
        this.error = err.error || 'Error al registrar deuda';
        this.cargando = false;
      }
    });
  }
}
