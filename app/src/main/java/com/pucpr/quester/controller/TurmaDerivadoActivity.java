package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pucpr.quester.R;

public class TurmaDerivadoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma_derivado);

        setTitle("Turma");

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
        }
    }

    public void cardAlunoClicked(View view) {
        Intent i = new Intent(TurmaDerivadoActivity.this, TurmaAlunoActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    public void cardDisciplinaClicked(View view) {
        Intent i = new Intent(TurmaDerivadoActivity.this, TurmaDisciplinaActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    public void cardProfessorClicked(View view) {
        Intent i = new Intent(TurmaDerivadoActivity.this, TurmaProfessorActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TurmaDerivadoActivity.this, TurmaActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}