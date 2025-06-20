import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Especialidad } from '../models/especialidad.model';

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {
  private apiUrl = '/api/especialidades';

  constructor(private http: HttpClient) { }

  getEspecialidades(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(this.apiUrl);
  }

  createEspecialidad(especialidad: Especialidad): Observable<Especialidad> {
    return this.http.post<Especialidad>(this.apiUrl, especialidad);
  }
}
