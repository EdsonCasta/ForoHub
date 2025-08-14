package com.alura.foroHub.repository;

import com.alura.foroHub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Topico findByTituloAndMensaje(String titulo, String mensaje);

    List<Topico> findTop10ByOrderByFechaCreacionAsc();

    @Query("""
       SELECT t FROM Topico t
       WHERE (:curso IS NULL OR t.curso = :curso)
       AND (:ano IS NULL OR YEAR(t.fechaCreacion) = :ano)
       """)
    List<Topico> findByCursoAndAno(@Param("curso") String curso, @Param("ano") int ano);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso")
    List<Topico> findByCurso(@Param("curso") String curso);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :ano")
    List<Topico> findByAnio(@Param("ano") Integer ano);
}
