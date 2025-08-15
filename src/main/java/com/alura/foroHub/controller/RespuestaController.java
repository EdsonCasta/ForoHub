package com.alura.foroHub.controller;

import com.alura.foroHub.dto.DatosRegistroRespuesta;
import com.alura.foroHub.dto.DatosRespuestaRespuesta;
import com.alura.foroHub.model.Respuesta;
import com.alura.foroHub.model.Topico;
import com.alura.foroHub.repository.RespuestaRepository;
import com.alura.foroHub.repository.TopicoRepository;
import com.alura.foroHub.usuario.Usuario;
import com.alura.foroHub.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    private final RespuestaRepository respuestaRepository;
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;

    public RespuestaController(
            RespuestaRepository respuestaRepository,
            TopicoRepository topicoRepository,
            UsuarioRepository usuarioRepository) {
        this.respuestaRepository = respuestaRepository;
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @Transactional
    public DatosRespuestaRespuesta registrarRespuesta(
            @RequestBody DatosRegistroRespuesta datos,
            Authentication authentication) {

        Usuario usuario = (Usuario) authentication.getPrincipal();
        Topico topico = topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(datos.mensaje());
        respuesta.setTopico(topico);
        respuesta.setUsuario(usuario);

        respuestaRepository.save(respuesta);

        return new DatosRespuestaRespuesta(
                respuesta.getId(),
                respuesta.getMensaje(),
                usuario.getUsuario(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion()
        );
    }

    @GetMapping("/topico/{id}")
    public List<DatosRespuestaRespuesta> listarPorTopico(@PathVariable Long id) {
        return respuestaRepository.findByTopicoId(id)
                .stream()
                .map(respuesta -> new DatosRespuestaRespuesta(
                        respuesta.getId(),
                        respuesta.getMensaje(),
                        respuesta.getUsuario().getUsuario(),
                        respuesta.getFechaCreacion(),
                        respuesta.getSolucion()
                ))
                .toList();
    }
}
