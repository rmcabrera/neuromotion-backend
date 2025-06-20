import { Component, OnInit, ViewChild } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms'; 
import { DoctorService } from '../services/doctor.service';
import { UsuarioService } from '../services/usuario.service';
import { CitaService } from '../services/cita.service';
import { Doctor } from '../models/doctor.model';
import { DoctorResponse } from '../models/doctor-response.model';
import { Usuario } from '../models/usuario.model';
import { Cita } from '../models/cita.model';
import { UserModalComponent } from '../user-modal/user-modal.component';
import { DoctorModalComponent } from '../doctor-modal/doctor-modal.component';
import { ConfirmationModalComponent } from '../confirmation-modal/confirmation-modal.component'; 
import { CitasListModalComponent } from '../citas-list-modal/citas-list-modal.component'; 

@Component({
  selector: 'app-neuromotion-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, UserModalComponent, DoctorModalComponent, ConfirmationModalComponent, CitasListModalComponent], 
  templateUrl: './neuromotion-dashboard.component.html',
  styleUrl: './neuromotion-dashboard.component.css'
})
export class NeuromotionDashboardComponent implements OnInit {
  // Doctores
  doctores: DoctorResponse[] = [];
  showAddDoctorModal: boolean = false;
  doctorToEdit: Doctor | null = null;

  // Usuarios
  usuarios: Usuario[] = [];
  showAddUserModal: boolean = false;
  userToEdit: Usuario | null = null;

  // Citas
  citas: Cita[] = [];
  newCita: Cita = { fechaHora: '', motivo: '', usuarioId: 0, doctorId: 0 };
  selectedUsuarioIdForCita: number = 0;
  selectedDoctorIdForCita: number = 0;

  tempFecha: string = '';
  tempHora: string = '';
  minDate: string;
  showConfirmationModal: boolean = false; 
  confirmationMessage: string = ''; 
  showCitasListModal: boolean = false; 

  @ViewChild('citaForm') citaForm!: NgForm; 

  constructor(
    private doctorService: DoctorService,
    private usuarioService: UsuarioService,
    private citaService: CitaService
  ) {
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];
    this.setDefaultDateTime(); 
  }

  ngOnInit(): void {
    this.loadDoctores();
    this.loadUsuarios();
    this.loadCitas();
  }

  // Métodos para Doctores
  loadDoctores(): void {
    this.doctorService.getDoctores().subscribe(
      data => this.doctores = data,
      error => console.error('Error loading doctores:', error)
    );
  }

  openAddDoctorModal(doctorResponse?: DoctorResponse | undefined): void {
    this.doctorToEdit = doctorResponse ? this.mapDoctorResponseToDoctor(doctorResponse) : null;
    this.showAddDoctorModal = true;
  }

  getDoctorToEdit(): DoctorResponse | undefined {
    return this.doctores.find(d => d.id === this.selectedDoctorIdForCita);
  }

  onDoctorModalClose(): void {
    this.showAddDoctorModal = false;
    this.doctorToEdit = null; // Clear doctorToEdit when modal closes
  }

  onDoctorSaved(doctor: Doctor): void {
    this.loadDoctores();
  }

  onDoctorDeleted(doctorId: number): void {
    this.loadDoctores();
  }

  private mapDoctorResponseToDoctor(doctorResponse: DoctorResponse): Doctor {
    return {
      id: doctorResponse.id,
      nombre: doctorResponse.nombre,
      licencia: doctorResponse.licencia,
      email: doctorResponse.email,
      especialidad: {
        id: doctorResponse.especialidadId,
        nombre: doctorResponse.especialidadNombre
      }
    };
  }

  // Métodos para Usuarios
  loadUsuarios(): void {
    this.usuarioService.getUsuarios().subscribe(
      data => this.usuarios = data,
      error => console.error('Error loading usuarios:', error)
    );
  }

  openAddUserModal(user?: Usuario | undefined): void {
    this.userToEdit = user || null;
    this.showAddUserModal = true;
  }

  getUsuarioToEdit(): Usuario | undefined {
    return this.usuarios.find(u => u.id === this.selectedUsuarioIdForCita);
  }

  onUserModalClose(): void {
    this.showAddUserModal = false;
    this.userToEdit = null; // Clear userToEdit when modal closes
  }

  onUserSaved(user: Usuario): void {
    this.loadUsuarios();
  }

  onUserDeleted(userId: number): void {
    this.loadUsuarios();
  }

  // Métodos para Citas
  loadCitas(): void {
    this.citaService.getCitas().subscribe(
      data => this.citas = data,
      error => console.error('Error loading citas:', error)
    );
  }

  addCita(): void {
    if (this.selectedUsuarioIdForCita === 0) {
      this.openConfirmationModal('Por favor, selecciona un usuario válido.');
      return;
    }
    if (this.selectedDoctorIdForCita === 0) {
      this.openConfirmationModal('Por favor, selecciona un doctor válido.');
      return;
    }
    if (!this.tempFecha || !this.tempHora) {
      this.openConfirmationModal('Por favor, selecciona una fecha y hora para la cita.');
      return;
    }
    if (!this.newCita.motivo) {
      this.openConfirmationModal('Por favor, ingresa el motivo de la cita.');
      return;
    }

    this.newCita.usuarioId = this.selectedUsuarioIdForCita;
    this.newCita.doctorId = this.selectedDoctorIdForCita;
    this.newCita.fechaHora = `${this.tempFecha}T${this.tempHora}:00`;

    const selectedDoctor = this.doctores.find(d => d.id === this.selectedDoctorIdForCita);
    if (selectedDoctor) {
      this.newCita.doctorEspecialidad = selectedDoctor.especialidadNombre;
    }

    this.citaService.createCita(this.newCita).subscribe(
      () => {
        this.loadCitas();
        this.resetCitaForm();
        this.openConfirmationModal('Cita creada exitosamente!'); 
      },
      error => console.error('Error creating cita:', error)
    );
  }

  openConfirmationModal(message: string): void {
    this.confirmationMessage = message;
    this.showConfirmationModal = true;
  }

  onConfirmationModalClose(): void {
    this.showConfirmationModal = false;
  }

  newCitaForm(): void {
    this.resetCitaForm();
  }

  private resetCitaForm(): void {
    this.newCita = { fechaHora: '', motivo: '', usuarioId: 0, doctorId: 0 };
    this.setDefaultDateTime();
    if (this.citaForm) {
      this.citaForm.resetForm();
    }
    // Ensure select values are explicitly reset after form reset
    this.selectedUsuarioIdForCita = 0;
    this.selectedDoctorIdForCita = 0;
  }

  private setDefaultDateTime(): void {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.tempFecha = tomorrow.toISOString().split('T')[0];
    this.tempHora = '08:00';
  }

  openCitasListModal(): void {
    this.showCitasListModal = true;
  }

  onCitasListModalClose(): void {
    this.showCitasListModal = false;
  }

  onDoctorError(message: string): void {
    this.openConfirmationModal(message);
  }

  onUserError(message: string): void {
    this.openConfirmationModal(message);
  }
}
