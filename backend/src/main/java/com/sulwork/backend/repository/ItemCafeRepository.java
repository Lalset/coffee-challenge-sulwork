package com.sulwork.backend.repository;

import com.sulwork.backend.model.ItemCafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemCafeRepository extends JpaRepository<ItemCafe, Long> {

    @Query("SELECT i FROM ItemCafe i WHERE i.descricao = :descricao AND i.dataCafe = :dataCafe")
    Optional<ItemCafe> findByDescricaoAndData(String descricao, LocalDate dataCafe);

    @Query("SELECT i FROM ItemCafe i WHERE i.colaborador.id = :colaboradorId")
    List<ItemCafe> findByColaboradorId(Long colaboradorId);

    List<ItemCafe> findByDataCafe(LocalDate dataCafe);
}
