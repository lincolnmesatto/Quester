package com.pucpr.quester.model;

public class TurmaDisciplinaModel {
    private String idTurma;
    private String idDisciplina;

    public TurmaDisciplinaModel() {
    }

    public TurmaDisciplinaModel(String idTurma, String idDisciplina) {
        this.idTurma = idTurma;
        this.idDisciplina = idDisciplina;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }
}
