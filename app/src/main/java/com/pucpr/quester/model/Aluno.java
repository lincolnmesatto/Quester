package com.pucpr.quester.model;

import java.util.List;

public class Aluno {
    private String id;
    private String idUsuario;
    private String idInsituicao;
    private Float xp;
    private int level;

    private List<String> turmas;

    public Aluno(){}

    public Aluno(String id, String idUsuario, String idInsituicao, Float xp, int level, List<String> turmas) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idInsituicao = idInsituicao;
        this.xp = xp;
        this.level = level;
        this.turmas = turmas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdInsituicao() {
        return idInsituicao;
    }

    public void setIdInsituicao(String idInsituicao) {
        this.idInsituicao = idInsituicao;
    }

    public Float getXp() {
        return xp;
    }

    public void setXp(Float xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<String> turmas) {
        this.turmas = turmas;
    }
}
