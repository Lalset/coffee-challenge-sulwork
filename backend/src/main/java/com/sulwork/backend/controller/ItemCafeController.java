package com.sulwork.backend.controller;

import com.sulwork.backend.exception.RegraNegocioException;
import com.sulwork.backend.model.ItemCafe;
import com.sulwork.backend.service.ItemCafeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/itens-cafe")
@CrossOrigin(origins = "*")
public class ItemCafeController {

    private final ItemCafeService itemCafeService;

    public ItemCafeController(ItemCafeService itemCafeService) {
        this.itemCafeService = itemCafeService;
    }

    // === LISTAR TODOS ===
    @GetMapping
    public ResponseEntity<List<ItemCafe>> listar() {
        return ResponseEntity.ok(itemCafeService.listar());
    }

    // === LISTAR POR DATA ===
    @GetMapping("/data/{data}")
    public ResponseEntity<?> listarPorData(@PathVariable String data) {
        try {
            return ResponseEntity.ok(itemCafeService.listarPorData(data));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === ATUALIZAR ITEM ===
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarItem(@PathVariable Long id, @RequestBody ItemCafe item) {
        try {
            item.setId(id);
            itemCafeService.atualizar(item);
            return ResponseEntity.ok("Item atualizado com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar item: " + e.getMessage());
        }
    }

    // === ATUALIZAR STATUS (trouxe / não trouxe) ===
    @PutMapping("/{id}/trouxe/{trouxe}")
    public ResponseEntity<String> atualizarStatus(@PathVariable Long id, @PathVariable boolean trouxe) {
        try {
            itemCafeService.atualizarStatus(id, trouxe);
            return ResponseEntity.ok("Status do item atualizado com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar status: " + e.getMessage());
        }
    }

    // === DELETAR ITEM ===
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarItem(@PathVariable Long id) {
        try {
            itemCafeService.deletar(id);
            return ResponseEntity.ok("Item deletado com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar item: " + e.getMessage());
        }
    }
}
