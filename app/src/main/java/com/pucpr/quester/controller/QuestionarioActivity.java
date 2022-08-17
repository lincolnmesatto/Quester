package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionarioActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> disciplinas;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionario);

        setTitle("Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        disciplinas = getIntent().getStringArrayListExtra("disciplinas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }
    }

    public void btnAddQuestionarioClicked(View view){
        Intent intent = new Intent(QuestionarioActivity.this, CadastrarQuestionarioActivity.class);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) disciplinas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idProfessor", idProfessor);
        intent.putExtra("idTurma", idTurma);
        startActivity(intent);
    }
}