import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReporteDiario } from '../models/reporte-diario.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReporteDiarioService {
  private apiUrl = `${environment.apiUrl}/reportes-diarios`;

  constructor(private http: HttpClient) {}

  listarPorMes(usuarioId: number, anio: number, mes: number): Observable<ReporteDiario[]> {
    return this.http.get<ReporteDiario[]>(`${this.apiUrl}/${usuarioId}/mes`, {
      params: { anio: anio.toString(), mes: mes.toString() }
    });
  }

  calcularPorMes(usuarioId: number, anio: number, mes: number): Observable<ReporteDiario[]> {
    return this.http.get<ReporteDiario[]>(`${this.apiUrl}/${usuarioId}/mes-calculado`, {
      params: { anio: anio.toString(), mes: mes.toString() }
    });
  }

  generarHoy(usuarioId: number): Observable<ReporteDiario> {
    return this.http.post<ReporteDiario>(`${this.apiUrl}/generar/${usuarioId}`, {});
  }
}
