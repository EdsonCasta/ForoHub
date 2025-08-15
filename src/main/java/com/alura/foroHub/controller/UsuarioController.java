package com.alura.foroHub.controller;

import com.alura.foroHub.dto.DatosDetalleUsuario;
import com.alura.foroHub.dto.DatosRegistroUsuario;
import com.alura.foroHub.usuario.Usuario;
import com.alura.foroHub.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DatosDetalleUsuario> registrarUsuario(@RequestBody DatosRegistroUsuario datos) {
        String contrasenaEncriptada = passwordEncoder.encode(datos.contrasena());
        Usuario usuario = new Usuario(null, datos.usuario(), contrasenaEncriptada);
        repository.save(usuario);
        return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    public List<DatosDetalleUsuario> listarUsuarios() {
        return repository.findAll().stream()
                .map(DatosDetalleUsuario::new)
                .toList();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<DatosDetalleUsuario> obtenerUsuario(@PathVariable Long id) {
        return repository.findById(id)
                .map(usuario -> ResponseEntity.ok(new DatosDetalleUsuario(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<DatosDetalleUsuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody DatosRegistroUsuario datos) {
        return repository.findById(id).map(usuario -> {

            usuario.setUsuario(datos.usuario());

            if (datos.contrasena() != null && !datos.contrasena().isBlank()) {
                if (!passwordEncoder.matches(datos.contrasena(), usuario.getContrasena())) {
                    usuario.setContrasena(passwordEncoder.encode(datos.contrasena()));
                }
            }
            repository.save(usuario);
            return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
