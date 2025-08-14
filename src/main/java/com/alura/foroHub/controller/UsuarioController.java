package com.alura.foroHub.controller;

import com.alura.foroHub.dto.DatosDetalleUsuario;
import com.alura.foroHub.dto.DatosRegistroUsuario;
import com.alura.foroHub.usuario.Usuario;
import com.alura.foroHub.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity<DatosDetalleUsuario> registrarUsuario(@RequestBody DatosRegistroUsuario datos) {
        Usuario usuario = new Usuario(null, datos.usuario(), datos.contrasena());
        repository.save(usuario);
        return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
    public List<DatosDetalleUsuario> listarUsuarios() {
        return repository.findAll().stream()
                .map(DatosDetalleUsuario::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleUsuario> obtenerUsuario(@PathVariable Long id) {
        return repository.findById(id)
                .map(usuario -> ResponseEntity.ok(new DatosDetalleUsuario(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosDetalleUsuario> actualizarUsuario(@PathVariable Long id, @RequestBody DatosRegistroUsuario datos) {
        return repository.findById(id).map(usuario -> {
            usuario = new Usuario(id, datos.usuario(), datos.contrasena());
            repository.save(usuario);
            return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
