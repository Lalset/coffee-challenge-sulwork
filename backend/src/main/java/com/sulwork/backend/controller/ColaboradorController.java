package com.sulwork.backend.controller;

import com.sulwork.backend.exception.RegraNegocioException;
import com.sulwork.backend.model.Colaborador;
import com.sulwork.backend.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "*")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    // === LISTAR TODOS ===
    @GetMapping
    public ResponseEntity<List<Colaborador>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // === CADASTRAR NOVO ===
    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody Colaborador colaborador) {
        try {
            service.cadastrar(colaborador);
            return ResponseEntity.ok("Colaborador cadastrado com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao cadastrar colaborador: " + e.getMessage());
        }
    }

    // === ATUALIZAR EXISTENTE ===
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody Colaborador colaborador) {
        try {
            service.atualizar(id, colaborador);
            return ResponseEntity.ok("Colaborador atualizado com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // === DELETAR ===
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.ok("Colaborador removido com sucesso!");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar colaborador: " + e.getMessage());
        }
    }
}
