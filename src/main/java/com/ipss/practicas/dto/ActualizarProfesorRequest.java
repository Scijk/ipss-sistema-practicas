package com.ipss.practicas.dto;

import jakarta.validation.constraints.Size;

public record ActualizarProfesorRequest(
        Long usuarioId,

        @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
        String especialidad,

        @Size(max = 80, message = "El cargo no puede exceder 80 caracteres")
        String cargo
) {
}
