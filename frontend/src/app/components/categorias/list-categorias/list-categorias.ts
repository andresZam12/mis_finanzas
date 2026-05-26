import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria.model';

@Component({
  selector: 'app-list-categorias',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './list-categorias.html'
})
export class ListCategoriasComponent implements OnInit {
  categorias: Categoria[] = [];
  cargando = true;
  eliminandoId: number | null = null;

  constructor(private categoriaService: CategoriaService) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    this.categoriaService.listarTodas().subscribe({
      next: c => { this.categorias = c; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar esta categoría?')) return;
    this.eliminandoId = id;
    this.categoriaService.eliminar(id).subscribe({
      next: () => { this.categorias = this.categorias.filter(c => c.id !== id); this.eliminandoId = null; },
      error: () => { this.eliminandoId = null; }
    });
  }
}
