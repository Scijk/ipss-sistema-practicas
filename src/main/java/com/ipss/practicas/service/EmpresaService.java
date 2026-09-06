package com.ipss.practicas.service;

import com.ipss.practicas.dto.ActualizarEmpresaRequest;
import com.ipss.practicas.dto.CrearEmpresaRequest;
import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Transactional(readOnly = true)
    public List<Empresa> listarTodos() {
        return empresaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empresa obtenerPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));
    }

    @Transactional
    public Empresa crear(CrearEmpresaRequest request) {
        validarDatos(request.nombre(), request.direccion(), request.telefono());

        if (request.email() != null && !request.email().isBlank() && empresaRepository.existsByEmail(request.email().trim())) {
            throw new BusinessException("Ya existe una empresa con ese email");
        }

        Empresa empresa = Empresa.builder()
                .nombre(request.nombre().trim())
                .direccion(request.direccion().trim())
                .telefono(request.telefono().trim())
                .email(request.email() == null || request.email().isBlank() ? null : request.email().trim())
                .descripcion(request.descripcion() == null || request.descripcion().isBlank() ? null : request.descripcion().trim())
                .build();

        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa actualizar(Long id, ActualizarEmpresaRequest request) {
        Empresa empresa = obtenerPorId(id);

        if (request.nombre() != null && !request.nombre().isBlank()) {
            empresa.setNombre(request.nombre().trim());
        }
        if (request.direccion() != null && !request.direccion().isBlank()) {
            empresa.setDireccion(request.direccion().trim());
        }
        if (request.telefono() != null && !request.telefono().isBlank()) {
            empresa.setTelefono(request.telefono().trim());
        }
        if (request.email() != null) {
            String email = request.email().isBlank() ? null : request.email().trim();
            if (email != null && empresaRepository.existsByEmail(email) && !email.equalsIgnoreCase(empresa.getEmail())) {
                throw new BusinessException("Ya existe una empresa con ese email");
            }
            empresa.setEmail(email);
        }
        if (request.descripcion() != null) {
            empresa.setDescripcion(request.descripcion().isBlank() ? null : request.descripcion().trim());
        }

        return empresaRepository.save(empresa);
    }

    @Transactional
    public void eliminar(Long id) {
        Empresa empresa = obtenerPorId(id);
        empresaRepository.delete(empresa);
    }

    private void validarDatos(String nombre, String direccion, String telefono) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre de la empresa es obligatorio");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new BusinessException("La dirección es obligatoria");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new BusinessException("El teléfono es obligatorio");
        }
    }
}
