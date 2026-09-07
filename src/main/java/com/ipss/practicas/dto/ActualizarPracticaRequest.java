package com.ipss.practicas.dto;

import java.time.LocalDate;

public record ActualizarPracticaRequest(
        Long estudianteId,
        Long profesorId,
        Long empresaId,
        Long jefeDirectoId,
        LocalDate fechaInicio,
        LocalDate fechaTermino,
        String descripcionActividades
) {
}
