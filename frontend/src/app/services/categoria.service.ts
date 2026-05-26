import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private apiUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  listarPorTipo(tipo: string): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.apiUrl}/tipo`, { params: { tipo } });
  }

  crear(nombre: string, tipo: string): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, null, { params: { nombre, tipo } });
  }

  actualizar(id: number, nombre: string, tipo: string): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.apiUrl}/${id}`, null, { params: { nombre, tipo } });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerPorId(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.apiUrl}/${id}`);
  }
}
