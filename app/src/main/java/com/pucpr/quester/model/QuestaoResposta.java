package com.pucpr.quester.model;

import java.util.List;

public class QuestaoResposta {
    private List<AlternativaResposta> alternativas;

    public QuestaoResposta() {
    }

    public QuestaoResposta(List<AlternativaResposta> alternativas) {
        this.alternativas = alternativas;
    }

    public List<AlternativaResposta> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<AlternativaResposta> alternativas) {
        this.alternativas = alternativas;
    }
}
