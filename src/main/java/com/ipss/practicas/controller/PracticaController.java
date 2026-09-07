package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarPracticaRequest;
import com.ipss.practicas.dto.CrearPracticaRequest;
import com.ipss.practicas.dto.EntityMapper;
import com.ipss.practicas.dto.PracticaResponse;
import com.ipss.practicas.service.PracticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public List<PracticaResponse> listarTodas() {
        return practicaService.listarTodas().stream()
                .map(EntityMapper::toPracticaResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PracticaResponse obtenerPorId(@PathVariable Long id) {
        return EntityMapper.toPracticaResponse(practicaService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<PracticaResponse> listarPorEstudiante(@PathVariable Long estudianteId) {
        return practicaService.listarPorEstudiante(estudianteId).stream()
                .map(EntityMapper::toPracticaResponse)
                .toList();
    }

    @GetMapping("/profesor/{profesorId}")
    public List<PracticaResponse> listarPorProfesor(@PathVariable Long profesorId) {
        return practicaService.listarPorProfesor(profesorId).stream()
                .map(EntityMapper::toPracticaResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PracticaResponse> crearPractica(@Valid @RequestBody CrearPracticaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityMapper.toPracticaResponse(practicaService.crearPractica(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticaResponse> actualizarPractica(@PathVariable Long id,
                                                           @Valid @RequestBody ActualizarPracticaRequest request) {
        return ResponseEntity.ok(EntityMapper.toPracticaResponse(practicaService.actualizarPractica(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PracticaResponse> actualizarParcialPractica(@PathVariable Long id,
                                                                   @Valid @RequestBody ActualizarPracticaRequest request) {
        return ResponseEntity.ok(EntityMapper.toPracticaResponse(practicaService.actualizarPractica(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build();
    }
}
