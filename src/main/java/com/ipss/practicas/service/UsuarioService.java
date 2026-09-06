package com.ipss.practicas.service;

import com.ipss.practicas.dto.ActualizarUsuarioRequest;
import com.ipss.practicas.dto.CrearUsuarioRequest;
import com.ipss.practicas.entity.Usuario;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional
    public Usuario crear(CrearUsuarioRequest request) {
        validarRequest(request.nombre(), request.apellido(), request.email(), request.password(), request.rol().name());

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .email(request.email().trim())
                .password(request.password())
                .rol(request.rol())
                .build();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, ActualizarUsuarioRequest request) {
        Usuario usuario = obtenerPorId(id);

        if (request.nombre() != null && !request.nombre().isBlank()) {
            usuario.setNombre(request.nombre().trim());
        }
        if (request.apellido() != null && !request.apellido().isBlank()) {
            usuario.setApellido(request.apellido().trim());
        }
        if (request.email() != null && !request.email().isBlank()) {
            String email = request.email().trim();
            if (!email.equalsIgnoreCase(usuario.getEmail()) && usuarioRepository.existsByEmail(email)) {
                throw new BusinessException("Ya existe un usuario con ese email");
            }
            usuario.setEmail(email);
        }
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(request.password());
        }
        if (request.rol() != null) {
            usuario.setRol(request.rol());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    private void validarRequest(String nombre, String apellido, String email, String password, String rol) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new BusinessException("El apellido es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new BusinessException("El email es obligatorio");
        }
        if (password == null || password.isBlank() || password.length() < 6) {
            throw new BusinessException("La contraseña debe tener al menos 6 caracteres");
        }
        if (rol == null || rol.isBlank()) {
            throw new BusinessException("El rol es obligatorio");
        }
    }
}
