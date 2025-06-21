package com.neuromotion.doctores.service;

import com.neuromotion.doctores.dto.DoctorResponseDTO;
import com.neuromotion.doctores.model.Doctor;
import com.neuromotion.doctores.model.Especialidad;
import com.neuromotion.doctores.repository.DoctorRepository;
import com.neuromotion.doctores.repository.EspecialidadRepository;
import com.neuromotion.doctores.feign.CitaFeignClient; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.neuromotion.doctores.dto.CitaResponseDTO; 
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private CitaFeignClient citaFeignClient; 

    @Override
    public List<Doctor> listarDoctores() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> obtenerPorId(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Optional<DoctorResponseDTO> obtenerDoctorDTOPorId(Long id) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (doctorOpt.isEmpty()) {
            return Optional.empty();
        }

        Doctor doctor = doctorOpt.get();

        Optional<Especialidad> especialidadOpt = especialidadRepository.findById(doctor.getEspecialidad().getId());

        String especialidadNombre = especialidadOpt.map(Especialidad::getNombre).orElse(null);

        DoctorResponseDTO dto = new DoctorResponseDTO(
                doctor.getId(),
                doctor.getNombre(),
                doctor.getLicencia(),
                doctor.getEmail(),
                doctor.getEspecialidad().getId(),
                especialidadNombre
        );

        return Optional.of(dto);
    }

    @Override
    public Doctor guardarDoctor(Doctor doctor) {
        if (doctorRepository.existsByLicencia(doctor.getLicencia())) {
            throw new RuntimeException("Ya existe un doctor con esa licencia");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Ya existe un doctor con ese email");
        }

        Long especialidadId = doctor.getEspecialidad().getId();
        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RuntimeException("Especialidad no existe con id: " + especialidadId));

        doctor.setEspecialidad(especialidad);

        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor actualizarDoctor(Long id, Doctor doctorActualizado) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        if (!doctor.getLicencia().equals(doctorActualizado.getLicencia())
                && doctorRepository.existsByLicencia(doctorActualizado.getLicencia())) {
            throw new RuntimeException("Ya existe un doctor con esa licencia");
        }

        if (!doctor.getEmail().equals(doctorActualizado.getEmail())
                && doctorRepository.existsByEmail(doctorActualizado.getEmail())) {
            throw new RuntimeException("Ya existe un doctor con ese email");
        }

        Long especialidadId = doctorActualizado.getEspecialidad().getId();
        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RuntimeException("Especialidad no existe con id: " + especialidadId));

        doctor.setNombre(doctorActualizado.getNombre());
        doctor.setEspecialidad(doctorActualizado.getEspecialidad());
        doctor.setLicencia(doctorActualizado.getLicencia());
        doctor.setEmail(doctorActualizado.getEmail());

        return doctorRepository.save(doctor);
    }

    @Override
    public void eliminarDoctor(Long id) {
        logger.info("Attempting to delete doctor with ID: {}", id);
        try {
            
            List<CitaResponseDTO> citasRelacionadas = citaFeignClient.listarPorDoctor(id);
            if (!citasRelacionadas.isEmpty()) {
                logger.warn("Doctor with ID {} has {} related appointments. Deletion prevented.", id, citasRelacionadas.size());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el doctor porque tiene citas relacionadas.");
            }
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            logger.error("Error checking for related appointments for doctor ID {}: {}", id, e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al verificar citas relacionadas para el doctor.", e);
        }
        doctorRepository.deleteById(id);
        logger.info("Doctor with ID {} deleted successfully.", id);
    }

    @Override
    public List<Doctor> buscarPorEspecialidad(String especialidad) {
        return doctorRepository.findByEspecialidadNombre(especialidad);
    }

}
