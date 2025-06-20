package com.neuromotion.doctores.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaResponseDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private String motivo;

    private Long usuarioId;
    private String usuarioNombre;

    private Long doctorId;
    private String doctorNombre;
    private String doctorEspecialidad;
}
