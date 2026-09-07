package com.ipss.practicas.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {
}
