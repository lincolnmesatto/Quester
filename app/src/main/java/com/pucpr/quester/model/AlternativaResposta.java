package com.pucpr.quester.model;

public class AlternativaResposta {
    private int correta;

    public AlternativaResposta() {
    }

    public AlternativaResposta(int correta) {
        this.correta = correta;
    }

    public int getCorreta() {
        return correta;
    }

    public void setCorreta(int correta) {
        this.correta = correta;
    }
}
