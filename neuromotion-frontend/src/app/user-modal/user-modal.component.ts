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
        (error: any) => console.error('Error updating user:', error)
      );
    } else {
      this.usuarioService.createUsuario(this.newUsuario).subscribe(
        (user: Usuario) => {
          this.userSaved.emit(user);
          this.resetForm();
          this.closeModal.emit();
        },
        (error: any) => console.error('Error creating user:', error)
      );
    }
  }

  deleteUser(): void {
    if (this.newUsuario.id) {
      this.usuarioService.deleteUsuario(this.newUsuario.id).subscribe(
        () => {
          this.userDeleted.emit(this.newUsuario.id!);
          this.resetForm();
          this.closeModal.emit();
        },
        (error: any) => console.error('Error deleting user:', error)
      );
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
