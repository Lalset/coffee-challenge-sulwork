package com.sulwork.backend.integration;

import com.sulwork.backend.model.Colaborador;
import com.sulwork.backend.repository.ColaboradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class ColaboradorIntegrationTest {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @BeforeEach
    public void setUp() {
        colaboradorRepository.deleteAll(); // Limpa o banco antes de cada teste
    }

    @Test
    public void testSalvarColaboradorComSucesso() {
        String nomeUnico = "João_" + System.currentTimeMillis();
        String cpfUnico = "123456789" + System.currentTimeMillis();

        Colaborador colaborador = new Colaborador(nomeUnico, cpfUnico, LocalDate.now());

        Colaborador savedColaborador = colaboradorRepository.save(colaborador);

        assertNotNull(savedColaborador.getId());
        assertEquals(nomeUnico, savedColaborador.getNome());
    }

    @Test
    public void testSalvarColaboradorComNomeDuplicado() {
        String nomeUnico = "João_" + System.currentTimeMillis();
        String cpfUnico = "123456789" + System.currentTimeMillis();

        Colaborador colaborador = new Colaborador(nomeUnico, cpfUnico, LocalDate.now());

        colaboradorRepository.save(colaborador); // Salva o primeiro colaborador

        // Tentar salvar com o mesmo nome
        Colaborador colaboradorDuplicado = new Colaborador(nomeUnico, "98765432100", LocalDate.now());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> colaboradorRepository.save(colaboradorDuplicado)); // Verifica se erro ocorre
    }
}
