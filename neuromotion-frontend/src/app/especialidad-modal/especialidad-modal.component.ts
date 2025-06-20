import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Especialidad } from '../models/especialidad.model';
import { EspecialidadService } from '../services/especialidad.service';

@Component({
  selector: 'app-especialidad-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './especialidad-modal.component.html',
  styleUrl: './especialidad-modal.component.css'
})
export class EspecialidadModalComponent {
  @Input() showModal: boolean = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() especialidadSaved = new EventEmitter<Especialidad>();

  newEspecialidad: Especialidad = { nombre: '' };

  constructor(private especialidadService: EspecialidadService) { }

  addEspecialidad(): void {
    this.especialidadService.createEspecialidad(this.newEspecialidad).subscribe(
      (especialidad) => {
        this.especialidadSaved.emit(especialidad);
        this.resetForm();
        this.onClose();
      },
      error => console.error('Error creating especialidad:', error)
    );
  }

  onClose(): void {
    this.resetForm();
    this.closeModal.emit();
  }

  private resetForm(): void {
    this.newEspecialidad = { nombre: '' };
  }
}
