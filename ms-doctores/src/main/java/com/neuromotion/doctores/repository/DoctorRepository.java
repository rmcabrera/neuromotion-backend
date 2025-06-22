package com.neuromotion.doctores.repository;

import com.neuromotion.doctores.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByLicencia(String licencia);

    Optional<Doctor> findByEmail(String email);

    boolean existsByLicencia(String licencia);

    boolean existsByEmail(String email);

    boolean existsByEspecialidadId(Long especialidadId);

    List<Doctor> findByEspecialidadNombre(String nombreEspecialidad);
}
