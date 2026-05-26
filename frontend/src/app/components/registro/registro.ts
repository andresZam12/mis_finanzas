import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.html'
})
export class RegistroComponent {
  form: FormGroup;
  error = '';
  exito = '';
  cargando = false;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      nombre:   ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    this.error = '';
    this.auth.registro(this.form.value).subscribe({
      next: () => {
        this.exito = 'Cuenta creada. Redirigiendo al login...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: err => {
        this.error = err.error || 'No se pudo crear la cuenta.';
        this.cargando = false;
      }
    });
  }
}
