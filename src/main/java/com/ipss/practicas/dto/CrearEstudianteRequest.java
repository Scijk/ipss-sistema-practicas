package com.ipss.practicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearEstudianteRequest(
        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotBlank(message = "La carrera es obligatoria")
        @Size(max = 100, message = "La carrera no puede exceder 100 caracteres")
        String carrera,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        String direccion
) {
}
