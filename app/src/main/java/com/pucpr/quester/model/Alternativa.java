package com.pucpr.quester.model;

public class Alternativa {
    private String alternativa;
    private int correta;

    public Alternativa() {
    }

    public Alternativa(String alternativa, int correta) {
        this.alternativa = alternativa;
        this.correta = correta;
    }

    public String getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }

    public int getCorreta() {
        return correta;
    }

    public void setCorreta(int correta) {
        this.correta = correta;
    }
}
