package com.ipss.practicas.service;

import com.ipss.practicas.dto.CrearPracticaRequest;
import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.entity.JefeDirecto;
import com.ipss.practicas.entity.Practica;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.enums.EstadoPractica;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.EmpresaRepository;
import com.ipss.practicas.repository.EstudianteRepository;
import com.ipss.practicas.repository.JefeDirectoRepository;
import com.ipss.practicas.repository.PracticaRepository;
import com.ipss.practicas.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticaService {

    private final PracticaRepository practicaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final EmpresaRepository empresaRepository;
    private final JefeDirectoRepository jefeDirectoRepository;

    @Transactional
    public Practica crearPractica(CrearPracticaRequest request) {
        validarDatos(request);

        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.estudianteId()));
        Profesor profesor = profesorRepository.findById(request.profesorId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id: " + request.profesorId()));
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + request.empresaId()));
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(request.jefeDirectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Jefe directo no encontrado con id: " + request.jefeDirectoId()));

        if (!jefeDirecto.getEmpresa().getId().equals(empresa.getId())) {
            throw new BusinessException("El jefe directo no pertenece a la empresa indicada");
        }

        Practica practica = Practica.builder()
                .fechaInicio(request.fechaInicio())
                .fechaTermino(request.fechaTermino())
                .descripcionActividades(request.descripcionActividades().trim())
                .estado(EstadoPractica.PENDIENTE)
                .estudiante(estudiante)
                .profesorSupervisor(profesor)
                .empresa(empresa)
                .jefeDirecto(jefeDirecto)
                .build();

        return practicaRepository.save(practica);
    }

    @Transactional(readOnly = true)
    public List<Practica> listarTodas() {
        return practicaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Practica obtenerPorId(Long id) {
        return practicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Práctica no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Practica> listarPorEstudiante(Long estudianteId) {
        estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + estudianteId));
        return practicaRepository.findByEstudianteId(estudianteId);
    }

    @Transactional(readOnly = true)
    public List<Practica> listarPorProfesor(Long profesorId) {
        profesorRepository.findById(profesorId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id: " + profesorId));
        return practicaRepository.findByProfesorSupervisorId(profesorId);
    }

    @Transactional
    public Practica actualizarPractica(Long id, CrearPracticaRequest request) {
        Practica practica = obtenerPorId(id);
        validarDatos(request);

        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.estudianteId()));
        Profesor profesor = profesorRepository.findById(request.profesorId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id: " + request.profesorId()));
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + request.empresaId()));
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(request.jefeDirectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Jefe directo no encontrado con id: " + request.jefeDirectoId()));

        if (!jefeDirecto.getEmpresa().getId().equals(empresa.getId())) {
            throw new BusinessException("El jefe directo no pertenece a la empresa indicada");
        }

        practica.setFechaInicio(request.fechaInicio());
        practica.setFechaTermino(request.fechaTermino());
        practica.setDescripcionActividades(request.descripcionActividades().trim());
        practica.setEstudiante(estudiante);
        practica.setProfesorSupervisor(profesor);
        practica.setEmpresa(empresa);
        practica.setJefeDirecto(jefeDirecto);

        return practicaRepository.save(practica);
    }

    @Transactional
    public void eliminarPractica(Long id) {
        Practica practica = obtenerPorId(id);
        practicaRepository.delete(practica);
    }

    private void validarDatos(CrearPracticaRequest request) {
        if (request == null) {
            throw new BusinessException("La solicitud es obligatoria");
        }
        if (request.estudianteId() == null) {
            throw new BusinessException("El estudiante es obligatorio");
        }
        if (request.profesorId() == null) {
            throw new BusinessException("El profesor es obligatorio");
        }
        if (request.empresaId() == null) {
            throw new BusinessException("La empresa es obligatoria");
        }
        if (request.jefeDirectoId() == null) {
            throw new BusinessException("El jefe directo es obligatorio");
        }
        if (request.fechaInicio() == null || request.fechaTermino() == null) {
            throw new BusinessException("Las fechas de inicio y término son obligatorias");
        }
        if (request.fechaTermino().isBefore(request.fechaInicio())) {
            throw new BusinessException("La fecha de término no puede ser menor que la de inicio");
        }
        if (request.descripcionActividades() == null || request.descripcionActividades().isBlank()) {
            throw new BusinessException("La descripción de actividades es obligatoria");
        }
    }
}
