package com.ipss.practicas.dto;

import com.ipss.practicas.enums.RolUsuario;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        RolUsuario rol
) {
}
