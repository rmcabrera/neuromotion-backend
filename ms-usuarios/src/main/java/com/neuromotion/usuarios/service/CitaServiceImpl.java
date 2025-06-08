package com.neuromotion.usuarios.service;

import com.neuromotion.usuarios.dto.CitaRequestDTO;
import com.neuromotion.usuarios.dto.CitaResponseDTO;
import com.neuromotion.usuarios.dto.DoctorDTO;
import com.neuromotion.usuarios.feign.DoctorFeignClient;
import com.neuromotion.usuarios.model.Cita;
import com.neuromotion.usuarios.model.Usuario;
import com.neuromotion.usuarios.repository.CitaRepository;
import com.neuromotion.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final DoctorFeignClient doctorFeignClient;
    private final UsuarioRepository usuarioRepository;

    public CitaServiceImpl(CitaRepository citaRepository, DoctorFeignClient doctorFeignClient, UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.doctorFeignClient = doctorFeignClient;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<CitaResponseDTO> listarCitasConDoctor() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::mapToDTO).toList();
    }

    @Override
    public CitaResponseDTO obtenerCitaConDoctorPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));
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
        DoctorDTO doctor;
        try {
            doctor = doctorFeignClient.obtenerDoctorPorId(dto.getDoctorId());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado con id: " + dto.getDoctorId());
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + dto.getUsuarioId()));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));

        DoctorDTO doctor;
        try {
            doctor = doctorFeignClient.obtenerDoctorPorId(dto.getDoctorId());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado con id: " + dto.getDoctorId());
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + dto.getUsuarioId()));

        citaExistente.setFechaHora(dto.getFechaHora());
        citaExistente.setMotivo(dto.getMotivo());
        citaExistente.setUsuarioId(dto.getUsuarioId());
        citaExistente.setDoctorId(dto.getDoctorId());

        return mapToDTO(citaRepository.save(citaExistente));
    }

    @Override
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la cita con id: " + id);
        }
        citaRepository.deleteById(id);
    }

    private CitaResponseDTO mapToDTO(Cita cita) {
        DoctorDTO doctor;
        try {
            doctor = doctorFeignClient.obtenerDoctorPorId(cita.getDoctorId());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado con id: " + cita.getDoctorId());
        }

        Usuario usuario = usuarioRepository.findById(cita.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + cita.getUsuarioId()));

        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setMotivo(cita.getMotivo());
        dto.setUsuarioId(cita.getUsuarioId());
        dto.setUsuarioNombre(usuario.getNombre() + " " + usuario.getApellido());

        dto.setDoctorId(doctor.getId());
        dto.setDoctorNombre(doctor.getNombre());
        dto.setDoctorEspecialidad(doctor.getEspecialidadNombre());

        return dto;
    }
}
