package com.alura.foroHub.repository;

import com.alura.foroHub.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    List<Respuesta> findByTopicoId(Long id);
}
