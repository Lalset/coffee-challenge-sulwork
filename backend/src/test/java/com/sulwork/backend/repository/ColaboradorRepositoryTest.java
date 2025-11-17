package com.sulwork.backend.repository;

import com.sulwork.backend.model.Colaborador;
import com.sulwork.backend.util.CpfGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ColaboradorRepositoryTest {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @BeforeEach
    void setup() {
        colaboradorRepository.deleteAll();

        Colaborador c1 = new Colaborador();
        c1.setNome("João Silva");
        c1.setCpf(CpfGenerator.gerarCpfValido());
        c1.setDataCafe(LocalDate.now());

        Colaborador c2 = new Colaborador();
        c2.setNome("Maria Souza");
        c2.setCpf(CpfGenerator.gerarCpfValido());
        c2.setDataCafe(LocalDate.now());

        colaboradorRepository.save(c1);
        colaboradorRepository.save(c2);
    }

    @Test
    void deveSalvarColaboradorComSucesso() {
        Colaborador novo = new Colaborador();
        novo.setNome("Carlos");
        novo.setCpf(CpfGenerator.gerarCpfValido());
        novo.setDataCafe(LocalDate.now());

        Colaborador salvo = colaboradorRepository.save(novo);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Carlos");
    }

    @Test
    void deveEncontrarPorId() {
        Colaborador novo = new Colaborador();
        novo.setNome("Ana");
        novo.setCpf(CpfGenerator.gerarCpfValido());
        novo.setDataCafe(LocalDate.now());

        Colaborador salvo = colaboradorRepository.save(novo);

        Optional<Colaborador> encontrado = colaboradorRepository.findById(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Ana");
    }

    @Test
    void deveListarTodos() {
        var lista = colaboradorRepository.findAll();
        assertThat(lista).hasSize(2);
    }
}
