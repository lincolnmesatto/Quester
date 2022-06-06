package com.pucpr.quester.model;

import java.util.List;

public class Professor {
    private String id;
    private String idUsuario;
    private String idInsituicao;

    private List<TurmaDisciplinaModel> tdm;

    public Professor() {
    }

    public Professor(String id, String idUsuario, String idInsituicao, List<TurmaDisciplinaModel>  tdm) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idInsituicao = idInsituicao;
        this.tdm = tdm;
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

    public List<TurmaDisciplinaModel>  getTdm() {
        return tdm;
    }

    public void setTdm(List<TurmaDisciplinaModel>  tdm) {
        this.tdm = tdm;
    }
}
