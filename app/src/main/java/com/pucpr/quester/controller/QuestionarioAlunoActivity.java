package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.QuestionarioListAdapter;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questionario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionarioAlunoActivity extends AppCompatActivity implements QuestionarioListAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<Questionario> questionarios;
    ArrayList<String> turmas;
    String idAluno;
    String idInstituicao;
    String idTurma;

    RecyclerView recyclerviewListQuestionarioAluno;
    QuestionarioListAdapter questionarioListAdapter;

    ArrayList<Disciplina> disciplinasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionario_aluno);

        setTitle("Questionários");
        recyclerviewListQuestionarioAluno = findViewById(R.id.recyclerviewListQuestionarioAluno);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        questionarios = new ArrayList<>();
        disciplinasList = new ArrayList<>();

        turmas = getIntent().getStringArrayListExtra("turmas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }

        popularListaQuestionario(idTurma);
    }

    private void popularListaQuestionario(String idTurma) {
        Query ref = firestore.collection("questionarios").whereEqualTo("idTurma", idTurma);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarQuestionario(task);
                }
            }
        });
    }

    private void buscarQuestionario(Task<QuerySnapshot> task) {
        List<Questionario> qs = Objects.requireNonNull(task.getResult().toObjects(Questionario.class));
        questionarios.addAll(qs);

        for (Questionario qt:questionarios) {
            popularListaDisciplina(qt.getIdDisciplina());
        }
    }

    @Override
    public void onItemClick(Questionario questionario, int posicao) {
        Intent intent = new Intent(QuestionarioAlunoActivity.this, ResponderQuestionarioActivity.class);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        intent.putExtra("idTurma", idTurma);
        intent.putExtra("idQuestionario", questionario.getId());
        intent.putStringArrayListExtra("turmas", turmas);
        startActivity(intent);
    }

    private void popularListaDisciplina(String disc) {
        Query ref = firestore.collection("disciplinas").whereEqualTo("id", disc);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarDisciplina(task);
                }
            }
        });
    }

    private void buscarDisciplina(Task<QuerySnapshot> task){
        List<Disciplina> ds = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
        Disciplina d = ds.get(0);
        disciplinasList.add(d);

        questionarioListAdapter = new QuestionarioListAdapter(this, questionarios, disciplinasList);

        recyclerviewListQuestionarioAluno.setHasFixedSize(true);
        recyclerviewListQuestionarioAluno.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListQuestionarioAluno.setAdapter(questionarioListAdapter);
    }
}