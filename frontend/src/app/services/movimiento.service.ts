import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ingreso, Gasto, MovimientoRequest } from '../models/movimiento.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  private apiUrl = `${environment.apiUrl}/movimientos`;

  constructor(private http: HttpClient) {}

  registrarIngreso(req: MovimientoRequest): Observable<Ingreso> {
    return this.http.post<Ingreso>(`${this.apiUrl}/ingreso`, req);
  }

  registrarGasto(req: MovimientoRequest): Observable<Gasto> {
    return this.http.post<Gasto>(`${this.apiUrl}/gasto`, req);
  }

  listarIngresos(usuarioId: number): Observable<Ingreso[]> {
    return this.http.get<Ingreso[]>(`${this.apiUrl}/ingresos/${usuarioId}`);
  }

  listarGastos(usuarioId: number): Observable<Gasto[]> {
    return this.http.get<Gasto[]>(`${this.apiUrl}/gastos/${usuarioId}`);
  }

  obtenerIngreso(id: number): Observable<Ingreso> {
    return this.http.get<Ingreso>(`${this.apiUrl}/ingreso/${id}`);
  }

  obtenerGasto(id: number): Observable<Gasto> {
    return this.http.get<Gasto>(`${this.apiUrl}/gasto/${id}`);
  }

  editarIngreso(id: number, req: MovimientoRequest): Observable<Ingreso> {
    return this.http.put<Ingreso>(`${this.apiUrl}/ingreso/${id}`, req);
  }

  editarGasto(id: number, req: MovimientoRequest): Observable<Gasto> {
    return this.http.put<Gasto>(`${this.apiUrl}/gasto/${id}`, req);
  }

  eliminarIngreso(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ingreso/${id}`);
  }

  eliminarGasto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/gasto/${id}`);
  }

  calcularBalance(usuarioId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/balance/${usuarioId}`);
  }
}
