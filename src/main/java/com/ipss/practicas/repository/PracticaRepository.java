package com.ipss.practicas.repository;

import com.ipss.practicas.entity.Practica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    List<Practica> findByEstudianteId(Long estudianteId);

    List<Practica> findByProfesorSupervisorId(Long profesorId);
}
