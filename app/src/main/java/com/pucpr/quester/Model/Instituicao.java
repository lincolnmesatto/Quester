package com.pucpr.quester.Model;

public class Instituicao {
    private String id;
    private String nome;
    private String estado;
    private String cidade;

    public Instituicao() {
    }

    public Instituicao(String id, String nome, String estado, String cidade) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.cidade = cidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
