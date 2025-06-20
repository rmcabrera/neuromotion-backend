import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Doctor } from '../models/doctor.model';
import { Especialidad } from '../models/especialidad.model';
import { DoctorService } from '../services/doctor.service';
import { EspecialidadService } from '../services/especialidad.service';
import { EspecialidadModalComponent } from '../especialidad-modal/especialidad-modal.component';

@Component({
  selector: 'app-doctor-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, EspecialidadModalComponent],
  templateUrl: './doctor-modal.component.html',
  styleUrl: './doctor-modal.component.css'
})
export class DoctorModalComponent implements OnInit {
  @Input() showModal: boolean = false;
  @Input() doctorToEdit: Doctor | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() doctorSaved = new EventEmitter<Doctor>();
  @Output() doctorDeleted = new EventEmitter<number>();
  @Output() doctorError = new EventEmitter<string>(); // New output for error messages

  newDoctor: Doctor = { nombre: '', licencia: '', email: '', especialidad: { nombre: '' } };
  isEditMode: boolean = false;
  especialidades: Especialidad[] = [];
  selectedEspecialidadIdForDoctor: number = 0;
  showEspecialidadModal: boolean = false;

  constructor(
    private doctorService: DoctorService,
    private especialidadService: EspecialidadService
  ) { }

  ngOnInit(): void {
    this.loadEspecialidades();
  }

  ngOnChanges(): void {
    if (this.doctorToEdit) {
      this.newDoctor = { ...this.doctorToEdit };
      this.selectedEspecialidadIdForDoctor = this.doctorToEdit.especialidad.id!;
      this.isEditMode = true;
    } else {
      this.resetForm();
      this.isEditMode = false;
    }
  }

  loadEspecialidades(): void {
    this.especialidadService.getEspecialidades().subscribe(
      data => this.especialidades = data,
      error => console.error('Error loading especialidades:', error)
    );
  }

  saveDoctor(): void {
    const selectedEspecialidad = this.especialidades.find(e => e.id === this.selectedEspecialidadIdForDoctor);
    if (selectedEspecialidad) {
      this.newDoctor.especialidad = selectedEspecialidad;
      if (this.isEditMode && this.newDoctor.id) {
        this.doctorService.updateDoctor(this.newDoctor.id, this.newDoctor).subscribe(
          (doctor: Doctor) => {
            this.doctorSaved.emit(doctor);
            this.resetForm();
            this.closeModal.emit();
          },
          (error: any) => console.error('Error updating doctor:', error)
        );
      } else {
        this.doctorService.createDoctor(this.newDoctor).subscribe(
          (doctor: Doctor) => {
            this.doctorSaved.emit(doctor);
            this.resetForm();
            this.closeModal.emit();
          },
          (error: any) => console.error('Error creating doctor:', error)
        );
      }
    } else {
      console.error('No especialidad selected for doctor.');
    }
  }

  deleteDoctor(): void {
    if (this.newDoctor.id) {
      this.doctorService.deleteDoctor(this.newDoctor.id).subscribe(
        () => {
          this.doctorDeleted.emit(this.newDoctor.id!);
          this.resetForm();
          this.closeModal.emit();
        },
        (error: any) => {
          console.error('Error deleting doctor:', error);
          const errorMessage = error.error?.message || 'Error desconocido al eliminar el doctor.';
          this.doctorError.emit(errorMessage);
          this.closeModal.emit(); // Close modal even on error
        }
      );
    }
  }

  openEspecialidadModal(): void {
    this.showEspecialidadModal = true;
  }

  onEspecialidadModalClose(): void {
    this.showEspecialidadModal = false;
  }

  onEspecialidadSaved(especialidad: Especialidad): void {
    this.loadEspecialidades();
    this.selectedEspecialidadIdForDoctor = especialidad.id!;
  }

  onClose(): void {
    this.resetForm();
    this.closeModal.emit();
  }

  private resetForm(): void {
    this.newDoctor = { nombre: '', licencia: '', email: '', especialidad: { nombre: '' } };
    this.selectedEspecialidadIdForDoctor = 0;
    this.doctorToEdit = null;
    this.isEditMode = false;
  }
}
