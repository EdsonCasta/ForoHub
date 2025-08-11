package com.alura.foroHub.dto;

import jakarta.validation.constraints.NotNull;

public record DatosTopico(
        @NotNull String titulo,
        @NotNull String mensaje,
        @NotNull String autor,
        @NotNull String curso
) {
}
