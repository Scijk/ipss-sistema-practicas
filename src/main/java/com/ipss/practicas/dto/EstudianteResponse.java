package com.ipss.practicas.dto;

public record EstudianteResponse(
        Long id,
        Long usuarioId,
        String carrera,
        String telefono,
        String direccion
) {
}
