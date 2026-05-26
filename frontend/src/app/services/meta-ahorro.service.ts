import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MetaAhorro, MetaAhorroRequest } from '../models/meta-ahorro.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MetaAhorroService {
  private apiUrl = `${environment.apiUrl}/metas`;

  constructor(private http: HttpClient) {}

  crearMeta(req: MetaAhorroRequest): Observable<MetaAhorro> {
    return this.http.post<MetaAhorro>(this.apiUrl, req);
  }

  obtenerMetaMes(usuarioId: number, mes: number, anio: number): Observable<MetaAhorro> {
    return this.http.get<MetaAhorro>(`${this.apiUrl}/${usuarioId}`, { params: { mes, anio } });
  }

  verificarCumplimiento(usuarioId: number, mes: number, anio: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${usuarioId}/verificar`, { params: { mes, anio } });
  }

  listarPorUsuario(usuarioId: number): Observable<MetaAhorro[]> {
    return this.http.get<MetaAhorro[]>(`${this.apiUrl}/${usuarioId}/todas`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
