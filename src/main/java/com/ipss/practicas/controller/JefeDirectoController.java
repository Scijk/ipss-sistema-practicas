package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarJefeDirectoRequest;
import com.ipss.practicas.dto.CrearJefeDirectoRequest;
import com.ipss.practicas.entity.JefeDirecto;
import com.ipss.practicas.service.JefeDirectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jefes-directos")
@RequiredArgsConstructor
public class JefeDirectoController {

    private final JefeDirectoService jefeDirectoService;

    @GetMapping
    public List<JefeDirecto> listarTodos() {
        return jefeDirectoService.listarTodos();
    }

    @GetMapping("/{id}")
    public JefeDirecto obtenerPorId(@PathVariable Long id) {
        return jefeDirectoService.obtenerPorId(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<JefeDirecto> listarPorEmpresa(@PathVariable Long empresaId) {
        return jefeDirectoService.listarPorEmpresa(empresaId);
    }

    @PostMapping
    public ResponseEntity<JefeDirecto> crear(@Valid @RequestBody CrearJefeDirectoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jefeDirectoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JefeDirecto> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody ActualizarJefeDirectoRequest request) {
        return ResponseEntity.ok(jefeDirectoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jefeDirectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
