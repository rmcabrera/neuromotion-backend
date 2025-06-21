package com.neuromotion.doctores.controller;

import com.neuromotion.doctores.dto.DoctorResponseDTO;
import com.neuromotion.doctores.model.Doctor;
import com.neuromotion.doctores.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
@Tag(name = "Doctores", description = "API para la gestión de doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Operation(summary = "Listar todos los doctores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctores listados exitosamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping
    public List<DoctorResponseDTO> listarDoctores() {
        return doctorService.listarDoctores().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Operation(summary = "Obtener doctor por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor encontrado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> obtenerDoctorPorId(@PathVariable Long id) {
        return doctorService.obtenerPorId(id)
                .map(doctor -> ResponseEntity.ok(mapToDTO(doctor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar doctores por especialidad",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctores listados por especialidad exitosamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/especialidad/{nombre}")
    public List<DoctorResponseDTO> buscarPorEspecialidad(@PathVariable String nombre) {
        return doctorService.buscarPorEspecialidad(nombre).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Operation(summary = "Crear un nuevo doctor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PostMapping
    public ResponseEntity<?> crearDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            Doctor guardado = doctorService.guardarDoctor(doctor);
            DoctorResponseDTO recargado = doctorService.obtenerDoctorDTOPorId(guardado.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor no encontrado después de guardar"));
            return ResponseEntity.ok(recargado);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Actualizar un doctor existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctor) {
        try {
            Doctor actualizado = doctorService.actualizarDoctor(id, doctor);
            DoctorResponseDTO recargado = doctorService.obtenerDoctorDTOPorId(actualizado.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor no encontrado después de actualizar"));
            return ResponseEntity.ok(recargado);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Eliminar un doctor",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Doctor eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDoctor(@PathVariable Long id) {
        try {
            doctorService.eliminarDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            
            throw ex;
        }
    }

    private DoctorResponseDTO mapToDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getNombre(),
                doctor.getLicencia(),
                doctor.getEmail(),
                doctor.getEspecialidad().getId(),
                doctor.getEspecialidad().getNombre()
        );
    }
}
