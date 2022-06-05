package com.pucpr.quester.model;

import java.util.List;

public class Disciplina {
    private String id;
    private String nome;
    private int isComum;

    private List<String> turmas;

    public Disciplina(){}

    public Disciplina(String id, String nome, int isComum, List<String> turmas) {
        this.id = id;
        this.nome = nome;
        this.isComum = isComum;
        this.turmas = turmas;
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

    public List<String> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<String> turmas) {
        this.turmas = turmas;
    }
}
