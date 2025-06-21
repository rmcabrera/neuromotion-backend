import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Cita } from '../models/cita.model';
import { CitaService } from '../services/cita.service';
import { DoctorService } from '../services/doctor.service';
import { UsuarioService } from '../services/usuario.service';
import { DoctorResponse } from '../models/doctor-response.model';
import { Usuario } from '../models/usuario.model';
import { map } from 'rxjs/operators';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-citas-list-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './citas-list-modal.component.html',
  styleUrl: './citas-list-modal.component.css'
})
export class CitasListModalComponent implements OnInit {
  @Input() showModal: boolean = false;
  @Output() closeModal = new EventEmitter<void>();

  citas: Cita[] = [];
  filteredCitas: Cita[] = [];
  searchTerm: string = '';
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 1;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  private allDoctores: DoctorResponse[] = [];
  private allUsuarios: Usuario[] = [];

  constructor(
    private citaService: CitaService,
    private doctorService: DoctorService,
    private usuarioService: UsuarioService
  ) { }

  ngOnInit(): void {
    // No need to load citas here, as ngOnChanges will handle it when modal opens
  }

  ngOnChanges(): void {
    if (this.showModal) {
      this.loadInitialData();
    }
  }

  loadInitialData(): void {
    forkJoin([
      this.doctorService.getDoctores(),
      this.usuarioService.getUsuarios(),
      this.citaService.getCitas()
    ]).pipe(
      map(([doctores, usuarios, citas]) => {
        this.allDoctores = doctores;
        this.allUsuarios = usuarios;
        return citas.map(cita => {
          const doctor = doctores.find(d => d.id === cita.doctorId);
          const usuario = usuarios.find(u => u.id === cita.usuarioId);
          return {
            ...cita,
            doctorNombre: doctor ? doctor.nombre : 'Desconocido',
            usuarioNombre: usuario ? `${usuario.nombre} ${usuario.apellido}` : 'Desconocido'
          };
        });
      })
    ).subscribe(
      (citasWithNames: Cita[]) => {
        this.citas = citasWithNames;
        this.applyFiltersAndSort();
      },
      error => console.error('Error loading initial data:', error)
    );
  }

  applyFiltersAndSort(): void {
    let tempCitas = [...this.citas];

    // Filter
    if (this.searchTerm) {
      const lowerCaseSearchTerm = this.searchTerm.toLowerCase();
      tempCitas = tempCitas.filter(cita =>
        cita.motivo.toLowerCase().includes(lowerCaseSearchTerm) ||
        cita.doctorNombre?.toLowerCase().includes(lowerCaseSearchTerm) ||
        cita.usuarioNombre?.toLowerCase().includes(lowerCaseSearchTerm) ||
        cita.fechaHora.toLowerCase().includes(lowerCaseSearchTerm)
      );
    }

    // Sort
    if (this.sortColumn) {
      tempCitas.sort((a, b) => {
        const valA = (a as any)[this.sortColumn];
        const valB = (b as any)[this.sortColumn];

        if (valA < valB) {
          return this.sortDirection === 'asc' ? -1 : 1;
        }
        if (valA > valB) {
          return this.sortDirection === 'asc' ? 1 : -1;
        }
        return 0;
      });
    }

    this.totalPages = Math.ceil(tempCitas.length / this.itemsPerPage);
    this.currentPage = Math.min(this.currentPage, this.totalPages || 1); // Ensure current page is valid
    this.filteredCitas = tempCitas;
    this.paginateCitas();
  }

  paginateCitas(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.filteredCitas = this.filteredCitas.slice(startIndex, endIndex);
  }

  onSearchChange(): void {
    this.currentPage = 1; // Reset to first page on search
    this.applyFiltersAndSort();
  }

  onSort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.applyFiltersAndSort();
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.applyFiltersAndSort();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.applyFiltersAndSort();
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.applyFiltersAndSort();
    }
  }

  getPages(): number[] {
    const pages: number[] = [];
    for (let i = 1; i <= this.totalPages; i++) {
      pages.push(i);
    }
    return pages;
  }

  onClose(): void {
    this.closeModal.emit();
  }
}
