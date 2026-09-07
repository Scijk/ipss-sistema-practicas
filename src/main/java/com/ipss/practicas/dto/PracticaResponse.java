package com.ipss.practicas.dto;

import com.ipss.practicas.enums.EstadoPractica;

import java.time.LocalDate;

public record PracticaResponse(
        Long id,
        LocalDate fechaInicio,
        LocalDate fechaTermino,
        String descripcionActividades,
        EstadoPractica estado,
        Long estudianteId,
        Long profesorId,
        Long empresaId,
        Long jefeDirectoId
) {
}
