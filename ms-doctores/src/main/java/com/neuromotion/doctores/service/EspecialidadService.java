package com.neuromotion.doctores.service;

import com.neuromotion.doctores.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadService {

    List<Especialidad> listarTodas();

    Optional<Especialidad> obtenerPorId(Long id);

    Especialidad guardar(Especialidad especialidad);

    Especialidad actualizar(Long id, Especialidad especialidad);

    void eliminar(Long id);
}
