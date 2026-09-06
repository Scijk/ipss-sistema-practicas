package com.ipss.practicas.service;

import com.ipss.practicas.dto.ActualizarProfesorRequest;
import com.ipss.practicas.dto.CrearProfesorRequest;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.entity.Usuario;
import com.ipss.practicas.enums.RolUsuario;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.ProfesorRepository;
import com.ipss.practicas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id: " + id));
    }

    @Transactional
    public Profesor crear(CrearProfesorRequest request) {
        validarDatos(request.usuarioId(), request.especialidad());

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.usuarioId()));

        if (usuario.getRol() != RolUsuario.PROFESOR) {
            throw new BusinessException("El usuario asociado debe tener rol PROFESOR");
        }

        if (profesorRepository.existsByUsuarioId(request.usuarioId())) {
            throw new BusinessException("Ya existe un profesor asociado a este usuario");
        }

        Profesor profesor = Profesor.builder()
                .usuario(usuario)
                .especialidad(request.especialidad().trim())
                .cargo(request.cargo() == null ? null : request.cargo().trim())
                .build();

        usuario.setProfesor(profesor);
        return profesorRepository.save(profesor);
    }

    @Transactional
    public Profesor actualizar(Long id, ActualizarProfesorRequest request) {
        Profesor profesor = obtenerPorId(id);

        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.usuarioId()));
            if (usuario.getRol() != RolUsuario.PROFESOR) {
                throw new BusinessException("El usuario asociado debe tener rol PROFESOR");
            }
            if (!usuario.getId().equals(profesor.getUsuario().getId()) && profesorRepository.existsByUsuarioId(request.usuarioId())) {
                throw new BusinessException("Ya existe un profesor asociado a este usuario");
            }
            profesor.setUsuario(usuario);
            usuario.setProfesor(profesor);
        }

        if (request.especialidad() != null && !request.especialidad().isBlank()) {
            profesor.setEspecialidad(request.especialidad().trim());
        }
        if (request.cargo() != null) {
            profesor.setCargo(request.cargo().isBlank() ? null : request.cargo().trim());
        }

        return profesorRepository.save(profesor);
    }

    @Transactional
    public void eliminar(Long id) {
        Profesor profesor = obtenerPorId(id);
        profesorRepository.delete(profesor);
    }

    private void validarDatos(Long usuarioId, String especialidad) {
        if (usuarioId == null) {
            throw new BusinessException("El usuario es obligatorio");
        }
        if (especialidad == null || especialidad.isBlank()) {
            throw new BusinessException("La especialidad es obligatoria");
        }
    }
}
