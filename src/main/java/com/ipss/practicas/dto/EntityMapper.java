package com.ipss.practicas.dto;

import com.ipss.practicas.entity.Empresa;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.entity.JefeDirecto;
import com.ipss.practicas.entity.Practica;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.entity.Usuario;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public static EstudianteResponse toEstudianteResponse(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        return new EstudianteResponse(
                estudiante.getId(),
                estudiante.getUsuario() != null ? estudiante.getUsuario().getId() : null,
                estudiante.getCarrera(),
                estudiante.getTelefono(),
                estudiante.getDireccion()
        );
    }

    public static ProfesorResponse toProfesorResponse(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        return new ProfesorResponse(
                profesor.getId(),
                profesor.getUsuario() != null ? profesor.getUsuario().getId() : null,
                profesor.getEspecialidad(),
                profesor.getCargo()
        );
    }

    public static EmpresaResponse toEmpresaResponse(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getDireccion(),
                empresa.getTelefono(),
                empresa.getEmail(),
                empresa.getDescripcion()
        );
    }

    public static JefeDirectoResponse toJefeDirectoResponse(JefeDirecto jefeDirecto) {
        if (jefeDirecto == null) {
            return null;
        }
        return new JefeDirectoResponse(
                jefeDirecto.getId(),
                jefeDirecto.getNombre(),
                jefeDirecto.getApellido(),
                jefeDirecto.getCargo(),
                jefeDirecto.getTelefono(),
                jefeDirecto.getEmail(),
                jefeDirecto.getEmpresa() != null ? jefeDirecto.getEmpresa().getId() : null
        );
    }

    public static PracticaResponse toPracticaResponse(Practica practica) {
        if (practica == null) {
            return null;
        }
        return new PracticaResponse(
                practica.getId(),
                practica.getFechaInicio(),
                practica.getFechaTermino(),
                practica.getDescripcionActividades(),
                practica.getEstado(),
                practica.getEstudiante() != null ? practica.getEstudiante().getId() : null,
                practica.getProfesorSupervisor() != null ? practica.getProfesorSupervisor().getId() : null,
                practica.getEmpresa() != null ? practica.getEmpresa().getId() : null,
                practica.getJefeDirecto() != null ? practica.getJefeDirecto().getId() : null
        );
    }
}
