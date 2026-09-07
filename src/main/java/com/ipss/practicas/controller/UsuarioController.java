package com.ipss.practicas.controller;

import com.ipss.practicas.dto.ActualizarUsuarioRequest;
import com.ipss.practicas.dto.CrearUsuarioRequest;
import com.ipss.practicas.dto.EntityMapper;
import com.ipss.practicas.dto.UsuarioResponse;
import com.ipss.practicas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos().stream()
                .map(EntityMapper::toUsuarioResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(@PathVariable Long id) {
        return EntityMapper.toUsuarioResponse(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody CrearUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityMapper.toUsuarioResponse(usuarioService.crear(request))); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody ActualizarUsuarioRequest request) {
        return ResponseEntity.ok(EntityMapper.toUsuarioResponse(usuarioService.actualizar(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarParcial(@PathVariable Long id,
                                                         @Valid @RequestBody ActualizarUsuarioRequest request) {
        return ResponseEntity.ok(EntityMapper.toUsuarioResponse(usuarioService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
