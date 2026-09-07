package com.ipss.practicas.dto;

public record ProfesorResponse(
        Long id,
        Long usuarioId,
        String especialidad,
        String cargo
) {
}
