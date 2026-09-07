package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarEstudianteRequest;
import com.ipss.practicas.dto.CrearEstudianteRequest;
import com.ipss.practicas.dto.EntityMapper;
import com.ipss.practicas.dto.EstudianteResponse;
import com.ipss.practicas.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public List<EstudianteResponse> listarTodos() {
        return estudianteService.listarTodos().stream()
                .map(EntityMapper::toEstudianteResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EstudianteResponse obtenerPorId(@PathVariable Long id) {
        return EntityMapper.toEstudianteResponse(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody CrearEstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityMapper.toEstudianteResponse(estudianteService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ActualizarEstudianteRequest request) {
        return ResponseEntity.ok(EntityMapper.toEstudianteResponse(estudianteService.actualizar(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteResponse> actualizarParcial(@PathVariable Long id,
                                                           @Valid @RequestBody ActualizarEstudianteRequest request) {
        return ResponseEntity.ok(EntityMapper.toEstudianteResponse(estudianteService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
