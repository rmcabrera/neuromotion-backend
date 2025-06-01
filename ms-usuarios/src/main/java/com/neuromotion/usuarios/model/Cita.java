package com.neuromotion.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(length = 500)
    private String motivo;
}
