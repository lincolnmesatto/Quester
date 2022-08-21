package com.pucpr.quester.model;

import java.util.List;

public class Questionario {
    private String id;
    private String titulo;
    private int tipoPontuacao;
    private String idDisciplina;
    private String idTurma;
    private String idProfessor;
    private List<Questao> questoes;
    private double xp;

    public Questionario() {
    }

    public Questionario(String id, String titulo, int tipoPontuacao, String idDisciplina, String idTurma, String idProfessor, List<Questao> questoes, double xp) {
        this.id = id;
        this.titulo = titulo;
        this.tipoPontuacao = tipoPontuacao;
        this.idDisciplina = idDisciplina;
        this.idTurma = idTurma;
        this.idProfessor = idProfessor;
        this.questoes = questoes;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTipoPontuacao() {
        return tipoPontuacao;
    }

    public void setTipoPontuacao(int tipoPontuacao) {
        this.tipoPontuacao = tipoPontuacao;
    }
}
