package com.neuromotion.usuarios.service;

import com.neuromotion.usuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> obtenerPorId(Long id);
    Usuario guardarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
    void eliminarUsuario(Long id);
}
