package com.neuromotion.usuarios.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaRequestDTO {
    @NotNull
    @Future
    private LocalDateTime fechaHora;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long doctorId;

    private String motivo;
}
