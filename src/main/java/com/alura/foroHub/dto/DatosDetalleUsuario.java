package com.alura.foroHub.dto;

import com.alura.foroHub.usuario.Usuario;

public record DatosDetalleUsuario(
        Long id,
        String usuario
) {
    public DatosDetalleUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getUsuario());
    }
}
