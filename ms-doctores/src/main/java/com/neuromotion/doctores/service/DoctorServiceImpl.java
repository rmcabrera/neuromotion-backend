package com.neuromotion.doctores.service;

import com.neuromotion.doctores.dto.DoctorResponseDTO;
import com.neuromotion.doctores.model.Doctor;
import com.neuromotion.doctores.model.Especialidad;
import com.neuromotion.doctores.repository.DoctorRepository;
import com.neuromotion.doctores.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

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
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Doctor> buscarPorEspecialidad(String especialidad) {
        return doctorRepository.findByEspecialidadNombre(especialidad);
    }

}
