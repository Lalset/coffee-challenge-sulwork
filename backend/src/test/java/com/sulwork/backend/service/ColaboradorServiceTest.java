package com.sulwork.backend.service;

import com.sulwork.backend.exception.RegraNegocioException;
import com.sulwork.backend.model.Colaborador;
import com.sulwork.backend.model.ItemCafe;
import com.sulwork.backend.repository.ColaboradorRepository;
import com.sulwork.backend.repository.ItemCafeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ColaboradorServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepo;

    @Mock
    private ItemCafeRepository itemCafeRepo;

    @InjectMocks
    private ColaboradorService colaboradorService;

    private Colaborador colaborador;

    @BeforeEach
    public void setUp() {
        colaborador = new Colaborador("João", "12345678901", LocalDate.now().plusDays(1)); // Um colaborador válido
    }

    @Test
    public void testCadastrarColaboradorComCpfInvalido() {
        colaborador.setCpf("1234"); // CPF inválido

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.cadastrar(colaborador);
        });

        assertEquals("CPF inválido. Deve conter 11 dígitos numéricos.", exception.getMessage());
    }

    @Test
    public void testCadastrarColaboradorComNomeDuplicado() {
        // Mockando o repositório para que o nome "João" já exista
        when(colaboradorRepo.findByNome("João")).thenReturn(Optional.of(colaborador));

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.cadastrar(colaborador);
        });

        assertEquals("Nome já cadastrado.", exception.getMessage());
    }

    @Test
    public void testCadastrarColaboradorComCpfDuplicado() {
        // Mockando o repositório para que o CPF "12345678901" já exista
        when(colaboradorRepo.findByCpf("12345678901")).thenReturn(Optional.of(colaborador));

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.cadastrar(colaborador);
        });

        assertEquals("CPF já cadastrado.", exception.getMessage());
    }

    @Test
    public void testCadastrarColaboradorComDataCafeInvalida() {
        colaborador.setDataCafe(LocalDate.now().minusDays(1)); // Data inválida, anterior à data atual

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.cadastrar(colaborador);
        });

        assertEquals("A data do café deve ser maior que a data atual.", exception.getMessage());
    }

    @Test
    public void testCadastrarColaboradorComSucesso() {
        // Mockando o repositório para não encontrar nenhum colaborador com o mesmo nome ou CPF
        when(colaboradorRepo.findByNome(colaborador.getNome())).thenReturn(Optional.empty());
        when(colaboradorRepo.findByCpf(colaborador.getCpf())).thenReturn(Optional.empty());
        when(colaboradorRepo.save(any(Colaborador.class))).thenReturn(colaborador);

        Colaborador result = colaboradorService.cadastrar(colaborador);

        assertNotNull(result);
        assertEquals(colaborador.getNome(), result.getNome());
        assertEquals(colaborador.getCpf(), result.getCpf());
    }

    @Test
    public void testAtualizarColaborador() {
        Colaborador colaboradorExistente = new Colaborador("João", "12345678901", LocalDate.now().plusDays(1));
        colaboradorExistente.setId(1L);
        
        // Mockando repositórios
        when(colaboradorRepo.findById(1L)).thenReturn(Optional.of(colaboradorExistente));
        when(colaboradorRepo.save(any(Colaborador.class))).thenReturn(colaboradorExistente);

        // Novo colaborador para atualizar
        Colaborador colaboradorAtualizado = new Colaborador("João Atualizado", "12345678901", LocalDate.now().plusDays(2));
        
        // Mockando os itens de café
        when(itemCafeRepo.findByColaboradorId(1L)).thenReturn(null);

        Colaborador result = colaboradorService.atualizar(1L, colaboradorAtualizado);

        assertNotNull(result);
        assertEquals("João Atualizado", result.getNome());
    }

    @Test
    public void testAtualizarColaboradorNaoEncontrado() {
        // Mockando repositório para não encontrar o colaborador
        when(colaboradorRepo.findById(1L)).thenReturn(Optional.empty());

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.atualizar(1L, colaborador);
        });

        assertEquals("Colaborador não encontrado.", exception.getMessage());
    }

    @Test
    public void testCadastrarComItemCafeDuplicado() {
        ItemCafe itemCafe = new ItemCafe();
        itemCafe.setDescricao("Café Expresso");
        itemCafe.setDataCafe(LocalDate.now().plusDays(1));

        colaborador.getItensCafe().add(itemCafe);

        // Mockando o repositório para que já exista um item de café com a mesma descrição e data
        when(itemCafeRepo.findByDescricaoAndData("Café Expresso", LocalDate.now().plusDays(1))).thenReturn(Optional.of(itemCafe));

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            colaboradorService.cadastrar(colaborador);
        });

        assertEquals("O item 'Café Expresso' já foi cadastrado nesta data!", exception.getMessage());
    }
}
