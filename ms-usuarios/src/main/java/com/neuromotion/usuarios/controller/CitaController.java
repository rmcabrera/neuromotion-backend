package com.neuromotion.usuarios.controller;

import com.neuromotion.usuarios.dto.CitaRequestDTO;
import com.neuromotion.usuarios.dto.CitaResponseDTO;
import com.neuromotion.usuarios.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "API para la gestión de citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @Operation(summary = "Listar todas las citas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Citas listadas exitosamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitasConDoctor());
    }

    @Operation(summary = "Obtener cita por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cita encontrada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerCitaConDoctorPorId(id));
    }

    @Operation(summary = "Listar citas por ID de usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Citas listadas exitosamente por usuario"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.listarPorUsuarioIdConDoctor(usuarioId));
    }

    @Operation(summary = "Listar citas por ID de doctor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Citas listadas exitosamente por doctor"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(citaService.listarPorDoctorIdConDoctor(doctorId));
    }

    @Operation(summary = "Crear una nueva cita",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cita creada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.guardarCita(dto));
    }

    @Operation(summary = "Actualizar una cita existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }

    @Operation(summary = "Eliminar una cita",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
