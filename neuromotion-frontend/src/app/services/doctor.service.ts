import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Doctor } from '../models/doctor.model';
import { DoctorResponse } from '../models/doctor-response.model'; 

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private apiUrl = '/api/doctores';

  constructor(private http: HttpClient) { }

  getDoctores(): Observable<DoctorResponse[]> { 
    return this.http.get<DoctorResponse[]>(this.apiUrl);
  }

  getDoctoresByEspecialidad(nombreEspecialidad: string): Observable<DoctorResponse[]> { 
    return this.http.get<DoctorResponse[]>(`${this.apiUrl}/especialidad/${nombreEspecialidad}`);
  }

  createDoctor(doctor: Doctor): Observable<Doctor> {
    return this.http.post<Doctor>(this.apiUrl, doctor);
  }

  updateDoctor(id: number, doctor: Doctor): Observable<Doctor> {
    return this.http.put<Doctor>(`${this.apiUrl}/${id}`, doctor);
  }

  deleteDoctor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
