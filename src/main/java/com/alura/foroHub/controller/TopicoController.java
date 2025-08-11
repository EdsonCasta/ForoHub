package com.alura.foroHub.controller;

import com.alura.foroHub.dto.DatosTopico;
import com.alura.foroHub.model.Topico;
import com.alura.foroHub.repository.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
