package com.sulwork.backend.service;

import com.sulwork.backend.exception.RegraNegocioException;
import com.sulwork.backend.model.ItemCafe;
import com.sulwork.backend.repository.ItemCafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemCafeService {

    @Autowired
    private ItemCafeRepository itemCafeRepository;

    public List<ItemCafe> listar() {
        return itemCafeRepository.findAll();
    }

    public List<ItemCafe> listarPorData(String data) {
        try {
            LocalDate dataConvertida = LocalDate.parse(data);
            return itemCafeRepository.findByDataCafe(dataConvertida);
        } catch (Exception e) {
            throw new RegraNegocioException("Data inválida. Use o formato yyyy-MM-dd.");
        }
    }

    public ItemCafe atualizar(ItemCafe item) {
        ItemCafe existente = itemCafeRepository.findById(item.getId())
                .orElseThrow(() -> new RegraNegocioException("Item de café não encontrado."));

        if (item.getDescricao() == null || item.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("A descrição do item é obrigatória.");
        }

        existente.setDescricao(item.getDescricao());
        existente.setDataCafe(item.getDataCafe());
        existente.setTrouxe(item.getTrouxe());

        return itemCafeRepository.save(existente);
    }

    public ItemCafe atualizarStatus(Long id, boolean trouxe) {
        ItemCafe existente = itemCafeRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Item de café não encontrado."));
        existente.setTrouxe(trouxe);
        return itemCafeRepository.save(existente);
    }

    public void deletar(Long id) {
        if (!itemCafeRepository.existsById(id)) {
            throw new RegraNegocioException("Item de café não encontrado para exclusão.");
        }
        itemCafeRepository.deleteById(id);
    }
}
