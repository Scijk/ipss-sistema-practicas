package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarEmpresaRequest;
import com.ipss.practicas.dto.CrearEmpresaRequest;
import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listarTodos() {
        return empresaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Empresa obtenerPorId(@PathVariable Long id) {
        return empresaService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Empresa> crear(@Valid @RequestBody CrearEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizar(@PathVariable Long id,
                                            @Valid @RequestBody ActualizarEmpresaRequest request) {
        return ResponseEntity.ok(empresaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
