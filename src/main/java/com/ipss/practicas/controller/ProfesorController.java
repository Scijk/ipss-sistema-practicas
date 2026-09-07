package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarProfesorRequest;
import com.ipss.practicas.dto.CrearProfesorRequest;
import com.ipss.practicas.dto.EntityMapper;
import com.ipss.practicas.dto.ProfesorResponse;
import com.ipss.practicas.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    public List<ProfesorResponse> listarTodos() {
        return profesorService.listarTodos().stream()
                .map(EntityMapper::toProfesorResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProfesorResponse obtenerPorId(@PathVariable Long id) {
        return EntityMapper.toProfesorResponse(profesorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfesorResponse> crear(@Valid @RequestBody CrearProfesorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityMapper.toProfesorResponse(profesorService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody ActualizarProfesorRequest request) {
        return ResponseEntity.ok(EntityMapper.toProfesorResponse(profesorService.actualizar(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfesorResponse> actualizarParcial(@PathVariable Long id,
                                                         @Valid @RequestBody ActualizarProfesorRequest request) {
        return ResponseEntity.ok(EntityMapper.toProfesorResponse(profesorService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
