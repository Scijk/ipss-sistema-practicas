package com.ipss.practicas.controller;

import com.ipss.practicas.dto.CrearPracticaRequest;
import com.ipss.practicas.entity.Practica;
import com.ipss.practicas.service.PracticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/practicas")
@RequiredArgsConstructor
public class PracticaController {

    private final PracticaService practicaService;

    @GetMapping
    public List<Practica> listarTodas() {
        return practicaService.listarTodas();
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<Practica> listarPorEstudiante(@PathVariable Long estudianteId) {
        return practicaService.listarPorEstudiante(estudianteId);
    }

    @GetMapping("/profesor/{profesorId}")
    public List<Practica> listarPorProfesor(@PathVariable Long profesorId) {
        return practicaService.listarPorProfesor(profesorId);
    }

    @PostMapping
    public ResponseEntity<Practica> crearPractica(@Valid @RequestBody CrearPracticaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(practicaService.crearPractica(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practica> actualizarPractica(@PathVariable Long id,
                                                     @Valid @RequestBody CrearPracticaRequest request) {
        return ResponseEntity.ok(practicaService.actualizarPractica(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build();
    }
}
