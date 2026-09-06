package com.ipss.practicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CrearPracticaRequest(
        @NotNull Long estudianteId,
        @NotNull Long profesorId,
        @NotNull Long empresaId,
        @NotNull Long jefeDirectoId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaTermino,
        @NotBlank String descripcionActividades
) {
}
