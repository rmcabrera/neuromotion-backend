package com.neuromotion.doctores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private String nombre;
    private String licencia;
    private String email;
    private Long especialidadId;
    private String especialidadNombre;
}
