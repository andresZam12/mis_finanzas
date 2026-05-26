import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CategoriaService } from '../../../services/categoria.service';

@Component({
  selector: 'app-create-categoria',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-categoria.html'
})
export class CreateCategoriaComponent {
  form: FormGroup;
  error = '';
  exito = '';
  cargando = false;

  constructor(private fb: FormBuilder, private categoriaService: CategoriaService, private router: Router) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      tipo: ['INGRESO', Validators.required]
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const { nombre, tipo } = this.form.value;
    this.categoriaService.crear(nombre, tipo).subscribe({
      next: () => this.router.navigate(['/categorias']),
      error: err => {
        this.error = err.error || 'Error al crear categoría';
        this.cargando = false;
      }
    });
  }
}
