package com.neuromotion.doctores.service;

import com.neuromotion.doctores.dto.DoctorResponseDTO;
import com.neuromotion.doctores.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    List<Doctor> listarDoctores();

    Optional<Doctor> obtenerPorId(Long id);

    Optional<DoctorResponseDTO> obtenerDoctorDTOPorId(Long id);

    Doctor guardarDoctor(Doctor doctor);

    Doctor actualizarDoctor(Long id, Doctor doctorActualizado);

    void eliminarDoctor(Long id);

    List<Doctor> buscarPorEspecialidad(String especialidad);

}
