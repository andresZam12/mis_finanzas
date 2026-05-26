import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { CategoriaService } from '../../../services/categoria.service';

@Component({
  selector: 'app-edit-categoria',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './edit-categoria.html'
})
export class EditCategoriaComponent implements OnInit {
  form: FormGroup;
  error = '';
  cargando = false;
  id!: number;

  constructor(
    private fb: FormBuilder,
    private categoriaService: CategoriaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      tipo: ['INGRESO', Validators.required]
    });
  }

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.categoriaService.obtenerPorId(this.id).subscribe({
      next: c => this.form.patchValue({ nombre: c.nombre, tipo: c.tipo }),
      error: () => { this.error = 'No se pudo cargar la categoría.'; }
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const { nombre, tipo } = this.form.value;
    this.categoriaService.actualizar(this.id, nombre, tipo).subscribe({
      next: () => this.router.navigate(['/categorias']),
      error: err => { this.error = err.error || 'Error al guardar'; this.cargando = false; }
    });
  }
}
