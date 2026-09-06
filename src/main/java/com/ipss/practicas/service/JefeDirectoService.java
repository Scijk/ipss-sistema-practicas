package com.ipss.practicas.service;

import com.ipss.practicas.dto.ActualizarJefeDirectoRequest;
import com.ipss.practicas.dto.CrearJefeDirectoRequest;
import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.entity.JefeDirecto;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.exception.ResourceNotFoundException;
import com.ipss.practicas.repository.EmpresaRepository;
import com.ipss.practicas.repository.JefeDirectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JefeDirectoService {

    private final JefeDirectoRepository jefeDirectoRepository;
    private final EmpresaRepository empresaRepository;

    @Transactional(readOnly = true)
    public List<JefeDirecto> listarTodos() {
        return jefeDirectoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public JefeDirecto obtenerPorId(Long id) {
        return jefeDirectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jefe directo no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<JefeDirecto> listarPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + empresaId));
        return jefeDirectoRepository.findByEmpresaId(empresa.getId());
    }

    @Transactional
    public JefeDirecto crear(CrearJefeDirectoRequest request) {
        validarDatos(request.nombre(), request.apellido(), request.empresaId());

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + request.empresaId()));

        JefeDirecto jefeDirecto = JefeDirecto.builder()
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .cargo(request.cargo() == null || request.cargo().isBlank() ? null : request.cargo().trim())
                .telefono(request.telefono() == null || request.telefono().isBlank() ? null : request.telefono().trim())
                .email(request.email() == null || request.email().isBlank() ? null : request.email().trim())
                .empresa(empresa)
                .build();

        return jefeDirectoRepository.save(jefeDirecto);
    }

    @Transactional
    public JefeDirecto actualizar(Long id, ActualizarJefeDirectoRequest request) {
        JefeDirecto jefeDirecto = obtenerPorId(id);

        if (request.nombre() != null && !request.nombre().isBlank()) {
            jefeDirecto.setNombre(request.nombre().trim());
        }
        if (request.apellido() != null && !request.apellido().isBlank()) {
            jefeDirecto.setApellido(request.apellido().trim());
        }
        if (request.cargo() != null) {
            jefeDirecto.setCargo(request.cargo().isBlank() ? null : request.cargo().trim());
        }
        if (request.telefono() != null) {
            jefeDirecto.setTelefono(request.telefono().isBlank() ? null : request.telefono().trim());
        }
        if (request.email() != null) {
            jefeDirecto.setEmail(request.email().isBlank() ? null : request.email().trim());
        }
        if (request.empresaId() != null) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + request.empresaId()));
            jefeDirecto.setEmpresa(empresa);
        }

        return jefeDirectoRepository.save(jefeDirecto);
    }

    @Transactional
    public void eliminar(Long id) {
        JefeDirecto jefeDirecto = obtenerPorId(id);
        jefeDirectoRepository.delete(jefeDirecto);
    }

    private void validarDatos(String nombre, String apellido, Long empresaId) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new BusinessException("El apellido es obligatorio");
        }
        if (empresaId == null) {
            throw new BusinessException("La empresa es obligatoria");
        }
    }
}
