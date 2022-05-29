package com.pucpr.quester.model;

public class Recompensa {
    private String id;
    private String idInstituicao;
    private String descricao;
    private int levelAdquire;

    public Recompensa() {
    }

    public Recompensa(String id, String idInstituicao, String descricao, int levelAdquire) {
        this.id = id;
        this.idInstituicao = idInstituicao;
        this.descricao = descricao;
        this.levelAdquire = levelAdquire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(String idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLevelAdquire() {
        return levelAdquire;
    }

    public void setLevelAdquire(int levelAdquire) {
        this.levelAdquire = levelAdquire;
    }
}
