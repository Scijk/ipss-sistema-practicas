package com.ipss.practicas.dto;

import com.ipss.practicas.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioRequest(
        @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
        String nombre,

        @Size(max = 80, message = "El apellido no puede exceder 80 caracteres")
        String apellido,

        @Email(message = "Debe ingresar un email válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
        String password,

        RolUsuario rol
) {
}
