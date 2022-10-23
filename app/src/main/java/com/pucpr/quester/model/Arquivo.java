package com.pucpr.quester.model;

public class Arquivo {

    private String name;
    private String url;
    private String idTurma;
    private String idDisciplina;

    public Arquivo() {
    }

    public Arquivo(String name, String url, String idTurma, String idDisciplina) {
        this.name = name;
        this.url = url;
        this.idTurma = idTurma;
        this.idDisciplina = idDisciplina;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
