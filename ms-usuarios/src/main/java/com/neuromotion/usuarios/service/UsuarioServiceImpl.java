package com.neuromotion.usuarios.service;

import com.neuromotion.usuarios.model.Usuario;
import com.neuromotion.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.neuromotion.usuarios.dto.CitaResponseDTO; 
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final CitaService citaService; 

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CitaService citaService) {
        this.usuarioRepository = usuarioRepository;
        this.citaService = citaService;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);
        try {
            
            List<CitaResponseDTO> citasRelacionadas = citaService.listarPorUsuarioIdConDoctor(id);
            if (!citasRelacionadas.isEmpty()) {
                logger.warn("User with ID {} has {} related appointments. Deletion prevented.", id, citasRelacionadas.size());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el usuario porque tiene citas relacionadas.");
            }
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            logger.error("Error checking for related appointments for user ID {}: {}", id, e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al verificar citas relacionadas para el usuario.", e);
        }

        usuarioRepository.deleteById(id);
        logger.info("User with ID {} deleted successfully.", id);
    }
}
