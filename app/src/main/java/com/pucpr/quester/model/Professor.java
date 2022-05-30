package com.pucpr.quester.model;

public class Professor {
    private String id;
    private String idUsuario;
    private String idInsituicao;

    public Professor() {
    }

    public Professor(String id, String idUsuario, String idInsituicao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idInsituicao = idInsituicao;
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
}
