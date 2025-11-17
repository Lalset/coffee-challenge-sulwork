package com.sulwork.backend.service;

import com.sulwork.backend.exception.RegraNegocioException;
import com.sulwork.backend.model.Colaborador;
import com.sulwork.backend.model.ItemCafe;
import com.sulwork.backend.repository.ColaboradorRepository;
import com.sulwork.backend.repository.ItemCafeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepo;

    @Autowired
    private ItemCafeRepository itemCafeRepo;

    public Colaborador cadastrar(Colaborador colaborador) {
        validarColaborador(colaborador);

        colaborador.getItensCafe().forEach(item -> {
            if (itemCafeRepo.findByDescricaoAndData(item.getDescricao(), item.getDataCafe()).isPresent()) {
                throw new RegraNegocioException("O item '" + item.getDescricao() + "' já foi cadastrado nesta data!");
            }
            item.setColaborador(colaborador);
        });

        return colaboradorRepo.save(colaborador);
    }

    private void validarColaborador(Colaborador c) {
        if (!c.getCpf().matches("\\d{11}")) {
            throw new RegraNegocioException("CPF inválido. Deve conter 11 dígitos numéricos.");
        }

        if (colaboradorRepo.findByCpf(c.getCpf()).isPresent())
            throw new RegraNegocioException("CPF já cadastrado.");

        if (colaboradorRepo.findByNome(c.getNome()).isPresent())
            throw new RegraNegocioException("Nome já cadastrado.");

        if (c.getDataCafe().isBefore(LocalDate.now().plusDays(1)))
            throw new RegraNegocioException("A data do café deve ser maior que a data atual.");
    }

    public List<Colaborador> listar() {
        return colaboradorRepo.findAll();
    }

    public void deletar(Long id) {
        colaboradorRepo.deleteById(id);
    }

    // ✅ Atualização robusta com sincronização real da data
    @Transactional
    public Colaborador atualizar(Long id, Colaborador atualizado) {
        Colaborador existente = colaboradorRepo.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Colaborador não encontrado."));

        boolean dataMudou = atualizado.getDataCafe() != null
                && !atualizado.getDataCafe().equals(existente.getDataCafe());

        existente.setNome(atualizado.getNome());
        existente.setCpf(atualizado.getCpf());
        existente.setDataCafe(atualizado.getDataCafe());

        colaboradorRepo.save(existente);

        // Se a data mudou, atualiza todos os itens associados no banco
        if (dataMudou) {
            List<ItemCafe> itensDoColaborador = itemCafeRepo.findByColaboradorId(existente.getId());
            for (ItemCafe item : itensDoColaborador) {
                item.setDataCafe(atualizado.getDataCafe());
                itemCafeRepo.save(item);
            }
        }

        // Atualiza ou adiciona novos itens que vieram no body
        if (atualizado.getItensCafe() != null && !atualizado.getItensCafe().isEmpty()) {
            for (ItemCafe item : atualizado.getItensCafe()) {
                item.setColaborador(existente);

                if (item.getId() == null) {
                    itemCafeRepo.save(item);
                } else {
                    ItemCafe existenteItem = itemCafeRepo.findById(item.getId())
                            .orElseThrow(() -> new RegraNegocioException("Item de café não encontrado."));
                    existenteItem.setDescricao(item.getDescricao());
                    existenteItem.setTrouxe(item.getTrouxe());
                    existenteItem.setDataCafe(existente.getDataCafe());
                    itemCafeRepo.save(existenteItem);
                }
            }
        }

        return existente;
    }
}
