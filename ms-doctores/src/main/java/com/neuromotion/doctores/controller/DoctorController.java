package com.neuromotion.doctores.controller;

import com.neuromotion.doctores.dto.DoctorResponseDTO;
import com.neuromotion.doctores.model.Doctor;
import com.neuromotion.doctores.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<DoctorResponseDTO> listarDoctores() {
        return doctorService.listarDoctores().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> obtenerDoctorPorId(@PathVariable Long id) {
        return doctorService.obtenerPorId(id)
                .map(doctor -> ResponseEntity.ok(mapToDTO(doctor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{nombre}")
    public List<DoctorResponseDTO> buscarPorEspecialidad(@PathVariable String nombre) {
        return doctorService.buscarPorEspecialidad(nombre).stream()
                .map(this::mapToDTO)
                .toList();
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDoctor(@PathVariable Long id) {
        doctorService.eliminarDoctor(id);
        return ResponseEntity.noContent().build();
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
