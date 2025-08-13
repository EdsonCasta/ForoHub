package com.alura.foroHub.dto;

import jakarta.validation.constraints.NotNull;

public record DatosAutenticacion(
        @NotNull String usuario,
        @NotNull String contrasena
) {
}
