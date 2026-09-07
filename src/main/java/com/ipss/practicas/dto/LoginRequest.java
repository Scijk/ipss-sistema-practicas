package com.ipss.practicas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ingresar un email válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
