package com.pucpr.quester.model;

import java.util.List;

public class Questao {
    private String enunciado;
    private List<Alternativa> alternativas;

    public Questao() {
    }

    public Questao(String enunciado, List<Alternativa> alternativas) {
        this.enunciado = enunciado;
        this.alternativas = alternativas;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }
}
