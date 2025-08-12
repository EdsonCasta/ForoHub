package com.alura.foroHub.controller;

import com.alura.foroHub.dto.DatosDetalleTopico;
import com.alura.foroHub.dto.DatosTopico;
import com.alura.foroHub.model.Topico;
import com.alura.foroHub.repository.TopicoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity crearTopico(@RequestBody @Valid DatosTopico datos) {

        Topico existente = repository.findByTituloAndMensaje(datos.titulo(), datos.mensaje());
        if (existente != null) {
            return ResponseEntity.badRequest().body("Ya existe un tópico con el mismo título y mensaje");
        }

        Topico nuevoTopico = new Topico(
                datos.titulo(),
                datos.mensaje(),
                datos.autor(),
                datos.curso()
        );

        repository.save(nuevoTopico);

        return ResponseEntity.ok("Topico creado correctamente");
    }

    @GetMapping
    public ResponseEntity<List<Topico>> listarTopicos() {
        List<Topico> topicos = repository.findAll();
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("El campo ID es obligatorio y debe ser mayor que cero");
        }

        var topico = repository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Topico no encontrado"));

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosTopico datos) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body("El campo ID es obligatorio y debe ser mayor que cero");
        }

        Optional<Topico> topicoId = repository.findById(id);

        if (!topicoId.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tópico no encontrado");
        }

        Topico topico = topicoId.get();
        topico.actualizarInfo(datos);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

}
