package com.pucpr.quester.model;

public class Disciplina {
    private String id;
    private String nome;
    private int isComum;

    public Disciplina(){}

    public Disciplina(String id, String nome, int isComum) {
        this.id = id;
        this.nome = nome;
        this.isComum = isComum;
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

    public int getIsComum() {
        return isComum;
    }

    public void setIsComum(int isComum) {
        this.isComum = isComum;
    }
}
