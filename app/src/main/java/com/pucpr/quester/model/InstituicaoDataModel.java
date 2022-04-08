package com.pucpr.quester.model;

import android.content.Context;

import java.util.ArrayList;

public class InstituicaoDataModel {
    private static final InstituicaoDataModel instance = new InstituicaoDataModel();

    private ArrayList<Instituicao> instituicoes;
    private Context context;
    private int posicao;

    private InstituicaoDataModel(){
        instituicoes = new ArrayList<>();
    }

    public static InstituicaoDataModel getInstance(){
        return instance;
    }

    public void setContext(Context context){
        this.context = context;

        if(instituicoes == null || instituicoes.size() <= 0)
            instituicoes = new ArrayList<>();
    }

    public ArrayList<Instituicao> getInstituicoes() {
        return instituicoes;
    }

    public void setInstituicoes(ArrayList<Instituicao> instituicoes) {
        this.instituicoes = instituicoes;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public Context getContext() {
        return context;
    }
}
