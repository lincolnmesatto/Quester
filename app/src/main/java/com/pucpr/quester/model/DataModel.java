package com.pucpr.quester.model;

import android.content.Context;

import java.util.ArrayList;

public class DataModel {
    private static final DataModel instance = new DataModel();

    private ArrayList<Questao> questoesDataModel;
    private Context context;
    private int posicao;
    private boolean insert;

    private DataModel(){
        questoesDataModel = new ArrayList<>();
    }
    public static DataModel getInstance(){
        return instance;
    }

    public void setContext(Context context){
        this.context = context;

        if(questoesDataModel == null || questoesDataModel.size() <= 0)
            questoesDataModel = new ArrayList<>();
    }

    public ArrayList<Questao> getQuestoesDataModel() {
        return questoesDataModel;
    }

    public void setQuestoesDataModel(ArrayList<Questao> questoesDataModel) {
        this.questoesDataModel = questoesDataModel;
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

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }
}
