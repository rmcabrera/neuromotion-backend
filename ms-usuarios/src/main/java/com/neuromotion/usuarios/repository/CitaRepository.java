package com.neuromotion.usuarios.repository;

import com.neuromotion.usuarios.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuarioId(Long usuarioId);
    List<Cita> findByDoctorId(Long doctorId);
}
