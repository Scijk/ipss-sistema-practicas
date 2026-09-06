package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarProfesorRequest;
import com.ipss.practicas.dto.CrearProfesorRequest;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    public List<Profesor> listarTodos() {
        return profesorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Profesor obtenerPorId(@PathVariable Long id) {
        return profesorService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Profesor> crear(@Valid @RequestBody CrearProfesorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody ActualizarProfesorRequest request) {
        return ResponseEntity.ok(profesorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
