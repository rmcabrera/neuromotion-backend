import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../models/usuario.model';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css'
})
export class UserModalComponent {
  @Input() showModal: boolean = false;
  @Input() userToEdit: Usuario | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() userSaved = new EventEmitter<Usuario>();
  @Output() userDeleted = new EventEmitter<number>();
  @Output() userError = new EventEmitter<string>(); 

  newUsuario: Usuario = { nombre: '', apellido: '', email: '', telefono: '' };
  isEditMode: boolean = false;

  constructor(private usuarioService: UsuarioService) { }

  ngOnChanges(): void {
    if (this.userToEdit) {
      this.newUsuario = { ...this.userToEdit };
      this.isEditMode = true;
    } else {
      this.resetForm();
      this.isEditMode = false;
    }
  }

  saveUser(): void {
    if (this.isEditMode && this.newUsuario.id) {
      this.usuarioService.updateUsuario(this.newUsuario.id, this.newUsuario).subscribe(
        (user: Usuario) => {
          this.userSaved.emit(user);
          this.resetForm();
          this.closeModal.emit();
        },
        (error: any) => {
          console.error('Error updating user:', error);
          let errorMessage = 'Error desconocido al actualizar el usuario.';
          if (typeof error.error === 'string') {
            errorMessage = error.error;
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }
          this.userError.emit(errorMessage);
        }
      );
    } else {
      this.usuarioService.createUsuario(this.newUsuario).subscribe(
        (user: Usuario) => {
          this.userSaved.emit(user);
          this.resetForm();
          this.closeModal.emit();
        },
        (error: any) => {
          console.error('Error creating user:', error);
          let errorMessage = 'Error desconocido al crear el usuario.';
          if (typeof error.error === 'string') {
            errorMessage = error.error;
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }
          this.userError.emit(errorMessage);
        }
      );
    }
  }

  deleteUser(): void {
    if (this.newUsuario.id) {
      this.userDeleted.emit(this.newUsuario.id!);
     
    }
  }

  onClose(): void {
    this.resetForm();
    this.closeModal.emit();
  }

  private resetForm(): void {
    this.newUsuario = { nombre: '', apellido: '', email: '', telefono: '' };
    this.userToEdit = null;
    this.isEditMode = false;
  }
}
