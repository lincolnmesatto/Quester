package com.pucpr.quester.model;

public class RecompensaAluno {
    private String id;
    private String idAluno;
    private String idRecompensa;
    private boolean resgatada;

    public RecompensaAluno(String id, String idAluno, String idRecompensa, boolean resgatada) {
        this.id = id;
        this.idAluno = idAluno;
        this.idRecompensa = idRecompensa;
        this.resgatada = resgatada;
    }

    public RecompensaAluno() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    public String getIdRecompensa() {
        return idRecompensa;
    }

    public void setIdRecompensa(String idRecompensa) {
        this.idRecompensa = idRecompensa;
    }

    public boolean isResgatada() {
        return resgatada;
    }

    public void setResgatada(boolean resgatada) {
        this.resgatada = resgatada;
    }
}
