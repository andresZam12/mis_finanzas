import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ahorro, AhorroRequest } from '../models/ahorro.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AhorroService {
  private apiUrl = `${environment.apiUrl}/ahorros`;

  constructor(private http: HttpClient) {}

  registrar(req: AhorroRequest): Observable<Ahorro> {
    return this.http.post<Ahorro>(this.apiUrl, req);
  }

  listarPorUsuario(usuarioId: number): Observable<Ahorro[]> {
    return this.http.get<Ahorro[]>(`${this.apiUrl}/${usuarioId}`);
  }

  totalAhorros(usuarioId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${usuarioId}/total`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
