package com.neuromotion.doctores.controller;

import com.neuromotion.doctores.model.Especialidad;
import com.neuromotion.doctores.service.EspecialidadService;
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
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades", description = "API para la gestión de especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @Operation(summary = "Listar todas las especialidades",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Especialidades listadas exitosamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping
    public List<Especialidad> listar() {
        return especialidadService.listarTodas();
    }

    @Operation(summary = "Obtener especialidad por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Especialidad encontrada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtenerPorId(@PathVariable Long id) {
        return especialidadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva especialidad",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Especialidad creada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Especialidad especialidad) {
        try {
            return ResponseEntity.ok(especialidadService.guardar(especialidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una especialidad existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Especialidad especialidad) {
        try {
            return ResponseEntity.ok(especialidadService.actualizar(id, especialidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una especialidad",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Especialidad eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            especialidadService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }
}
