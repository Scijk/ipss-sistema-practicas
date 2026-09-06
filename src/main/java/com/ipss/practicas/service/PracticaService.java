package com.ipss.practicas.service;

import com.ipss.practicas.dto.CrearPracticaRequest;
import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.entity.JefeDirecto;
import com.ipss.practicas.entity.Practica;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.enums.EstadoPractica;
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
        if (request.fechaTermino().isBefore(request.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de término no puede ser menor que la de inicio");
        }

        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        Profesor profesor = profesorRepository.findById(request.profesorId())
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(request.jefeDirectoId())
                .orElseThrow(() -> new IllegalArgumentException("Jefe directo no encontrado"));

        Practica practica = Practica.builder()
                .fechaInicio(request.fechaInicio())
                .fechaTermino(request.fechaTermino())
                .descripcionActividades(request.descripcionActividades())
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
    public List<Practica> listarPorEstudiante(Long estudianteId) {
        return practicaRepository.findByEstudianteId(estudianteId);
    }

    @Transactional(readOnly = true)
    public List<Practica> listarPorProfesor(Long profesorId) {
        return practicaRepository.findByProfesorSupervisorId(profesorId);
    }

    @Transactional
    public Practica actualizarPractica(Long id, CrearPracticaRequest request) {
        Practica practica = practicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Práctica no encontrada"));

        if (request.fechaTermino().isBefore(request.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de término no puede ser menor que la de inicio");
        }

        practica.setFechaInicio(request.fechaInicio());
        practica.setFechaTermino(request.fechaTermino());
        practica.setDescripcionActividades(request.descripcionActividades());
        practica.setEstudiante(estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado")));
        practica.setProfesorSupervisor(profesorRepository.findById(request.profesorId())
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado")));
        practica.setEmpresa(empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada")));
        practica.setJefeDirecto(jefeDirectoRepository.findById(request.jefeDirectoId())
                .orElseThrow(() -> new IllegalArgumentException("Jefe directo no encontrado")));

        return practicaRepository.save(practica);
    }

    @Transactional
    public void eliminarPractica(Long id) {
        Practica practica = practicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Práctica no encontrada"));
        practicaRepository.delete(practica);
    }
}
