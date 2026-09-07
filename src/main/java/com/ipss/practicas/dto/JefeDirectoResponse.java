package com.ipss.practicas.dto;

public record JefeDirectoResponse(
        Long id,
        String nombre,
        String apellido,
        String cargo,
        String telefono,
        String email,
        Long empresaId
) {
}
