package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarEstudianteRequest;
import com.ipss.practicas.dto.CrearEstudianteRequest;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public List<Estudiante> listarTodos() {
        return estudianteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Estudiante obtenerPorId(@PathVariable Long id) {
        return estudianteService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@Valid @RequestBody CrearEstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody ActualizarEstudianteRequest request) {
        return ResponseEntity.ok(estudianteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
