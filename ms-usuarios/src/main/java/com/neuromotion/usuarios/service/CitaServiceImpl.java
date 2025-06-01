package com.neuromotion.usuarios.service;

import com.neuromotion.usuarios.dto.CitaRequestDTO;
import com.neuromotion.usuarios.dto.CitaResponseDTO;
import com.neuromotion.usuarios.dto.DoctorDTO;
import com.neuromotion.usuarios.feign.DoctorFeignClient;
import com.neuromotion.usuarios.model.Cita;
import com.neuromotion.usuarios.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final DoctorFeignClient doctorFeignClient;

    public CitaServiceImpl(CitaRepository citaRepository, DoctorFeignClient doctorFeignClient) {
        this.citaRepository = citaRepository;
        this.doctorFeignClient = doctorFeignClient;
    }

    @Override
    public List<CitaResponseDTO> listarCitasConDoctor() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::mapToDTO).toList();
    }

    @Override
    public CitaResponseDTO obtenerCitaConDoctorPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
        return mapToDTO(cita);
    }

    @Override
    public List<CitaResponseDTO> listarPorUsuarioIdConDoctor(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<CitaResponseDTO> listarPorDoctorIdConDoctor(Long doctorId) {
        return citaRepository.findByDoctorId(doctorId)
                .stream().map(this::mapToDTO).toList();
    }

    @Override
    public CitaResponseDTO guardarCita(CitaRequestDTO dto) {
        DoctorDTO doctor = doctorFeignClient.obtenerDoctorPorId(dto.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado con id: " + dto.getDoctorId());
        }

        Cita cita = new Cita();
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setUsuarioId(dto.getUsuarioId());
        cita.setDoctorId(dto.getDoctorId());

        return mapToDTO(citaRepository.save(cita));
    }

    @Override
    public CitaResponseDTO actualizarCita(Long id, CitaRequestDTO dto) {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));

        DoctorDTO doctor = doctorFeignClient.obtenerDoctorPorId(dto.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado con id: " + dto.getDoctorId());
        }

        citaExistente.setFechaHora(dto.getFechaHora());
        citaExistente.setMotivo(dto.getMotivo());
        citaExistente.setUsuarioId(dto.getUsuarioId());
        citaExistente.setDoctorId(dto.getDoctorId());

        return mapToDTO(citaRepository.save(citaExistente));
    }

    @Override
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("No existe la cita con id: " + id);
        }
        citaRepository.deleteById(id);
    }

    private CitaResponseDTO mapToDTO(Cita cita) {
        DoctorDTO doctor = doctorFeignClient.obtenerDoctorPorId(cita.getDoctorId());

        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setMotivo(cita.getMotivo());
        dto.setUsuarioId(cita.getUsuarioId());

        dto.setDoctorId(doctor.getId());
        dto.setDoctorNombre(doctor.getNombre());
        dto.setDoctorEspecialidad(doctor.getEspecialidadNombre());

        return dto;
    }
}
