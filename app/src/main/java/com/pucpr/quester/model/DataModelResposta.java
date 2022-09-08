package com.pucpr.quester.model;

import android.content.Context;

import java.util.ArrayList;

public class DataModelResposta {
    private static final DataModelResposta instance = new DataModelResposta();

    private ArrayList<QuestaoResposta> questoesRespostaDataModel;
    private ArrayList<Questao> questoesDataModel;
    private Context context;
    private int posicao;

    private DataModelResposta(){
        questoesDataModel = new ArrayList<>();
    }
    public static DataModelResposta getInstance(){
        return instance;
    }

    public void setContext(Context context){
        this.context = context;

        if(questoesDataModel == null || questoesDataModel.size() <= 0)
            questoesDataModel = new ArrayList<>();

        if(questoesRespostaDataModel == null || questoesRespostaDataModel.size() <= 0)
            questoesRespostaDataModel = new ArrayList<>();
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

    public ArrayList<QuestaoResposta> getQuestoesRespostaDataModel() {
        return questoesRespostaDataModel;
    }

    public void setQuestoesRespostaDataModel(ArrayList<QuestaoResposta> questoesRespostaDataModel) {
        this.questoesRespostaDataModel = questoesRespostaDataModel;
    }

    public ArrayList<Questao> getQuestoesDataModel() {
        return questoesDataModel;
    }

    public void setQuestoesDataModel(ArrayList<Questao> questoesDataModel) {
        this.questoesDataModel = questoesDataModel;
    }
}
