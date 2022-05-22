package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pucpr.quester.R;

public class InsituicaoDerivadoActivity extends AppCompatActivity {

    String idInstituicao;
    String nomeInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insituicao_derivado);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");

            setTitle(nomeInstituicao);
        }
    }

    public void buttonAddUserClicked(View view){
        Intent i = new Intent(InsituicaoDerivadoActivity.this, CadastrarUsuarioActivity.class);
        startActivity(i);
    }
    public void buttonAddTurmaClicked(View view){
        Intent i = new Intent(InsituicaoDerivadoActivity.this, TurmaActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        startActivity(i);
    }
}