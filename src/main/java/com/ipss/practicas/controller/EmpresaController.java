package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarEmpresaRequest;
import com.ipss.practicas.dto.CrearEmpresaRequest;
import com.ipss.practicas.dto.EmpresaResponse;
import com.ipss.practicas.dto.EntityMapper;
import com.ipss.practicas.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public List<EmpresaResponse> listarTodos() {
        return empresaService.listarTodos().stream()
                .map(EntityMapper::toEmpresaResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EmpresaResponse obtenerPorId(@PathVariable Long id) {
        return EntityMapper.toEmpresaResponse(empresaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> crear(@Valid @RequestBody CrearEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityMapper.toEmpresaResponse(empresaService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody ActualizarEmpresaRequest request) {
        return ResponseEntity.ok(EntityMapper.toEmpresaResponse(empresaService.actualizar(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmpresaResponse> actualizarParcial(@PathVariable Long id,
                                                         @Valid @RequestBody ActualizarEmpresaRequest request) {
        return ResponseEntity.ok(EntityMapper.toEmpresaResponse(empresaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
