package com.alura.foroHub.dto;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(
        Long id,
        String mensaje,
        String usuario,
        LocalDateTime fechaCreacion,
        Boolean solucion) {
}
