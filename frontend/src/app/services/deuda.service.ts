import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Deuda, DeudaRequest } from '../models/deuda.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class DeudaService {
  private apiUrl = `${environment.apiUrl}/deudas`;

  constructor(private http: HttpClient) {}

  registrar(req: DeudaRequest): Observable<Deuda> {
    return this.http.post<Deuda>(this.apiUrl, req);
  }

  editar(id: number, req: DeudaRequest): Observable<Deuda> {
    return this.http.put<Deuda>(`${this.apiUrl}/${id}`, req);
  }

  marcarPagada(id: number): Observable<Deuda> {
    return this.http.put<Deuda>(`${this.apiUrl}/${id}/pagar`, {});
  }

  abonar(id: number, monto: number): Observable<Deuda> {
    return this.http.post<Deuda>(`${this.apiUrl}/${id}/abonar`, null, { params: { monto } });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  listarPorUsuario(usuarioId: number): Observable<Deuda[]> {
    return this.http.get<Deuda[]>(`${this.apiUrl}/${usuarioId}`);
  }

  listarPorEstado(usuarioId: number, estado: string): Observable<Deuda[]> {
    return this.http.get<Deuda[]>(`${this.apiUrl}/${usuarioId}/estado`, { params: { estado } });
  }

  listarPorTipo(usuarioId: number, tipo: string): Observable<Deuda[]> {
    return this.http.get<Deuda[]>(`${this.apiUrl}/${usuarioId}/tipo`, { params: { tipo } });
  }
}
