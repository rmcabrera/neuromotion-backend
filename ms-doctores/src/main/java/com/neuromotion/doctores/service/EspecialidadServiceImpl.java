package com.neuromotion.doctores.service;

import com.neuromotion.doctores.model.Especialidad;
import com.neuromotion.doctores.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    @Override
    public Optional<Especialidad> obtenerPorId(Long id) {
        return especialidadRepository.findById(id);
    }

    @Override
    public Especialidad guardar(Especialidad especialidad) {
        if (especialidadRepository.existsByNombre(especialidad.getNombre())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Especialidad actualizar(Long id, Especialidad especialidadActualizada) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        if (!especialidad.getNombre().equals(especialidadActualizada.getNombre())
                && especialidadRepository.existsByNombre(especialidadActualizada.getNombre())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }

        especialidad.setNombre(especialidadActualizada.getNombre());
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void eliminar(Long id) {
        especialidadRepository.deleteById(id);
    }
}
