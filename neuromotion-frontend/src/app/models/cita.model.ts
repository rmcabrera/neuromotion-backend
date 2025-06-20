export interface Cita {
  id?: number;
  fechaHora: string; 
  motivo: string;
  usuarioId: number;
  usuarioNombre?: string;
  doctorId: number;
  doctorNombre?: string;
  doctorEspecialidad?: string; 
}
