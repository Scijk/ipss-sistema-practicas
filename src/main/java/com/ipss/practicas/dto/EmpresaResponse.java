package com.ipss.practicas.dto;

public record EmpresaResponse(
        Long id,
        String nombre,
        String direccion,
        String telefono,
        String email,
        String descripcion
) {
}
