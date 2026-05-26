import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReporteFinanciero } from '../models/reporte.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = `${environment.apiUrl}/reportes`;

  constructor(private http: HttpClient) {}

  generarReporteMensual(usuarioId: number, mes: number, anio: number): Observable<ReporteFinanciero> {
    return this.http.get<ReporteFinanciero>(`${this.apiUrl}/${usuarioId}`, { params: { mes, anio } });
  }

  calcularBalanceTotal(usuarioId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${usuarioId}/balance`);
  }
}
