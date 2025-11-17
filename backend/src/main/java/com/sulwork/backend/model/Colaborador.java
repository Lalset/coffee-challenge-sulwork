package com.sulwork.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colaboradores")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(name = "data_cafe", nullable = false)
    private LocalDate dataCafe;

    @JsonManagedReference 
    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCafe> itensCafe = new ArrayList<>();

    public Colaborador() {}

    public Colaborador(String nome, String cpf, LocalDate dataCafe) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataCafe = dataCafe;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataCafe() { return dataCafe; }
    public void setDataCafe(LocalDate dataCafe) { this.dataCafe = dataCafe; }

    public List<ItemCafe> getItensCafe() { return itensCafe; }
    public void setItensCafe(List<ItemCafe> itensCafe) { this.itensCafe = itensCafe; }
}
