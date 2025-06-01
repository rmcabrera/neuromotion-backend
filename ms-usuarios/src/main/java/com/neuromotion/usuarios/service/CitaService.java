package com.neuromotion.usuarios.service;

import com.neuromotion.usuarios.dto.CitaRequestDTO;
import com.neuromotion.usuarios.dto.CitaResponseDTO;


import java.util.List;


public interface CitaService {
    List<CitaResponseDTO> listarCitasConDoctor();
    CitaResponseDTO obtenerCitaConDoctorPorId(Long id);
    List<CitaResponseDTO> listarPorUsuarioIdConDoctor(Long usuarioId);
    List<CitaResponseDTO> listarPorDoctorIdConDoctor(Long doctorId);
    CitaResponseDTO guardarCita(CitaRequestDTO dto);
    CitaResponseDTO actualizarCita(Long id, CitaRequestDTO dto);

    void eliminarCita(Long id);


}
