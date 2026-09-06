package com.ipss.practicas.repository;

import com.ipss.practicas.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}
