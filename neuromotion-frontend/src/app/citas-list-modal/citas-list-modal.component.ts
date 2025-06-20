import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Cita } from '../models/cita.model';
import { CitaService } from '../services/cita.service';

@Component({
  selector: 'app-citas-list-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './citas-list-modal.component.html',
  styleUrl: './citas-list-modal.component.css'
})
export class CitasListModalComponent implements OnInit {
  @Input() showModal: boolean = false;
  @Output() closeModal = new EventEmitter<void>();

  citas: Cita[] = [];

  constructor(private citaService: CitaService) { }

  ngOnInit(): void {
    
  }

  ngOnChanges(): void {
    if (this.showModal) {
      this.loadCitas();
    }
  }

  loadCitas(): void {
    this.citaService.getCitas().subscribe(
      data => this.citas = data,
      error => console.error('Error loading citas:', error)
    );
  }

  onClose(): void {
    this.closeModal.emit();
  }
}
