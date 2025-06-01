package com.neuromotion.usuarios.dto;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String nombre;
    private String licencia;
    private String email;
    private Long especialidadId;
    private String especialidadNombre;
}
