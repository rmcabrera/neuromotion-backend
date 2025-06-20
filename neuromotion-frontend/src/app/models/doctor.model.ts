import { Especialidad } from './especialidad.model';

export interface Doctor {
  id?: number;
  nombre: string;
  licencia: string;
  email: string;
  especialidad: Especialidad; 
}
