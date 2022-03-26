package com.pucpr.quester.model.datamodel;

import android.content.Context;
import android.widget.Toast;

import com.pucpr.quester.model.database.InstituicaoDatabase;
import com.pucpr.quester.model.entity.Instituicao;

import java.util.ArrayList;

public class InstituicaoDataModel {
    private static InstituicaoDataModel instance = new InstituicaoDataModel();
    private InstituicaoDataModel(){

    }
    public static InstituicaoDataModel getInstance(){
        return instance;
    }
    private InstituicaoDatabase database;
    private ArrayList<Instituicao> instituicoes;
    private Context context;

    public void setContext(Context context){
        this.context = context;
        database = new InstituicaoDatabase(context);
        instituicoes = database.retrieveInstituicaoFromDB();
    }

    public ArrayList<Instituicao> getInstituicoes(){
        return instituicoes;
    }

    public void addInstituicao(Instituicao instituicao){
        long id = database.createInstituciaoInDB(instituicao);
        if(id > 0){
            instituicao.setId(id);
            instituicoes.add(instituicao);
        }else{
            Toast.makeText(context,"Add instituicao problem", Toast.LENGTH_LONG).show();
        }
    }

    public void updateInstituicao(Instituicao instituicao){
        long i = database.updateInstituicaoInDB(instituicao);
        if(i > 0){
            instituicoes.add(instituicao);
        }else{
            Toast.makeText(context,"Update city problem", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteInstituicao(Instituicao instituicao){
        long i = database.deleteInstituicaoInDB(instituicao.getId());
        if(i > 0){
            instituicoes.remove(instituicao);
        }else{
            Toast.makeText(context,"Delete city problem", Toast.LENGTH_LONG).show();
        }
    }
}
