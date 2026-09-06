package com.ipss.practicas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearJefeDirectoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 80, message = "El apellido no puede exceder 80 caracteres")
        String apellido,

        @Size(max = 80, message = "El cargo no puede exceder 80 caracteres")
        String cargo,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @Email(message = "Debe ingresar un email válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @NotNull(message = "La empresa es obligatoria")
        Long empresaId
) {
}
