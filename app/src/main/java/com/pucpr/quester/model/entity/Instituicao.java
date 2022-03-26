package com.pucpr.quester.model.entity;

public class Instituicao {
    private long id;
    private String nome;
    private String cidade;
    private String estado;
    private String email;

    public Instituicao() {
    }

    public Instituicao(long id, String nome, String cidade, String estado, String email) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

