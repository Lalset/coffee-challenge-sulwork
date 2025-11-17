package com.sulwork.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "itens_cafe")
public class ItemCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "data_cafe", nullable = false)
    private LocalDate dataCafe;

    @Column(nullable = false)
    private Boolean trouxe = false;

    @JsonBackReference // ✅ evita loop (lado "filho")
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    public ItemCafe() {}

    public ItemCafe(String descricao, LocalDate dataCafe, Boolean trouxe) {
        this.descricao = descricao;
        this.dataCafe = dataCafe;
        this.trouxe = trouxe;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataCafe() { return dataCafe; }
    public void setDataCafe(LocalDate dataCafe) { this.dataCafe = dataCafe; }

    public Boolean getTrouxe() { return trouxe; }
    public void setTrouxe(Boolean trouxe) { this.trouxe = trouxe; }

    public Colaborador getColaborador() { return colaborador; }
    public void setColaborador(Colaborador colaborador) { this.colaborador = colaborador; }
}
