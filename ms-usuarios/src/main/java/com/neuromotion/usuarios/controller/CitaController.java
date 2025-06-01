package com.neuromotion.usuarios.controller;

import com.neuromotion.usuarios.dto.CitaRequestDTO;
import com.neuromotion.usuarios.dto.CitaResponseDTO;
import com.neuromotion.usuarios.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitasConDoctor());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerCitaConDoctorPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.listarPorUsuarioIdConDoctor(usuarioId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(citaService.listarPorDoctorIdConDoctor(doctorId));
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.guardarCita(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
