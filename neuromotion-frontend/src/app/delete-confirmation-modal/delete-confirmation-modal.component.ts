import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-delete-confirmation-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delete-confirmation-modal.component.html',
  styleUrl: './delete-confirmation-modal.component.css'
})
export class DeleteConfirmationModalComponent {
  @Input() showModal: boolean = false;
  @Input() message: string = '';
  @Input() isUserDelete: boolean = false;
  @Input() isDoctorDelete: boolean = false;

  @Output() confirmDelete = new EventEmitter<void>();
  @Output() cancelDelete = new EventEmitter<void>();

  onConfirm(): void {
    this.confirmDelete.emit();
    this.showModal = false; 
  }

  onCancel(): void {
    this.cancelDelete.emit();
    this.isUserDelete = false; 
    this.isDoctorDelete = false; 
    this.showModal = false; 
  }
}
