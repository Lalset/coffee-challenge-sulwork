package com.sulwork.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sulwork.backend.model.Colaborador;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    @Query(value = "SELECT * FROM colaboradores WHERE cpf = ?1", nativeQuery = true)
    Optional<Colaborador> findByCpf(String cpf);

    @Query(value = "SELECT * FROM colaboradores WHERE nome = ?1", nativeQuery = true)
    Optional<Colaborador> findByNome(String nome);
}
