package com.projetoa3.api.repository;

import com.projetoa3.api.entities.Avistamento;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvistamentoRepository extends JpaRepository<Avistamento, Long>{
    @Modifying
    @Transactional
    @Query(value = "SELECT comentario FROM avistamentos WHERE id_desaparecido = :desaparecidoId", nativeQuery = true)
    List<String> findByDesaparecidoId(@Param("desaparecidoId") Long desaparecidoId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO avistamentos (id_desaparecido, comentario) VALUES (:desaparecidoId, :comentario)", nativeQuery = true)
    void insertAvistamento(@Param("desaparecidoId") Long desaparecidoId, @Param("comentario") String comentario);
}