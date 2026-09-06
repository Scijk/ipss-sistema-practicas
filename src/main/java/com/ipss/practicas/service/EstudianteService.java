package com.ipss.practicas.service;

import com.ipss.practicas.dto.ActualizarEstudianteRequest;
import com.ipss.practicas.dto.CrearEstudianteRequest;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.entity.Usuario;
import com.ipss.practicas.enums.RolUsuario;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.EstudianteRepository;
import com.ipss.practicas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
    }

    @Transactional
    public Estudiante crear(CrearEstudianteRequest request) {
        validarDatos(request.usuarioId(), request.carrera());

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.usuarioId()));

        if (usuario.getRol() != RolUsuario.ESTUDIANTE) {
            throw new BusinessException("El usuario asociado debe tener rol ESTUDIANTE");
        }

        if (estudianteRepository.existsByUsuarioId(request.usuarioId())) {
            throw new BusinessException("Ya existe un estudiante asociado a este usuario");
        }

        Estudiante estudiante = Estudiante.builder()
                .usuario(usuario)
                .carrera(request.carrera().trim())
                .telefono(request.telefono() == null ? null : request.telefono().trim())
                .direccion(request.direccion() == null ? null : request.direccion().trim())
                .build();

        usuario.setEstudiante(estudiante);
        return estudianteRepository.save(estudiante);
    }

    @Transactional
    public Estudiante actualizar(Long id, ActualizarEstudianteRequest request) {
        Estudiante estudiante = obtenerPorId(id);

        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.usuarioId()));
            if (usuario.getRol() != RolUsuario.ESTUDIANTE) {
                throw new BusinessException("El usuario asociado debe tener rol ESTUDIANTE");
            }
            if (!usuario.getId().equals(estudiante.getUsuario().getId()) && estudianteRepository.existsByUsuarioId(request.usuarioId())) {
                throw new BusinessException("Ya existe un estudiante asociado a este usuario");
            }
            estudiante.setUsuario(usuario);
            usuario.setEstudiante(estudiante);
        }

        if (request.carrera() != null && !request.carrera().isBlank()) {
            estudiante.setCarrera(request.carrera().trim());
        }
        if (request.telefono() != null) {
            estudiante.setTelefono(request.telefono().isBlank() ? null : request.telefono().trim());
        }
        if (request.direccion() != null) {
            estudiante.setDireccion(request.direccion().isBlank() ? null : request.direccion().trim());
        }

        return estudianteRepository.save(estudiante);
    }

    @Transactional
    public void eliminar(Long id) {
        Estudiante estudiante = obtenerPorId(id);
        estudianteRepository.delete(estudiante);
    }

    private void validarDatos(Long usuarioId, String carrera) {
        if (usuarioId == null) {
            throw new BusinessException("El usuario es obligatorio");
        }
        if (carrera == null || carrera.isBlank()) {
            throw new BusinessException("La carrera es obligatoria");
        }
    }
}
