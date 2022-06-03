package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pucpr.quester.R;

public class TurmaDisciplinaActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma_disciplina);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
        }
    }
}