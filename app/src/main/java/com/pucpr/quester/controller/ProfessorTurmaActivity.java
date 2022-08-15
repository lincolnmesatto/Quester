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
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfessorTurmaActivity extends AppCompatActivity implements ProfessorTurmaAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> turmas;

    List<Turma> listaTurmas;

    RecyclerView recyclerViewTurma;
    ProfessorTurmaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_turma);

        setTitle("Turmas");
        recyclerViewTurma = findViewById(R.id.recyclerViewProfessorTurmaList);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        listaTurmas = new ArrayList<>();

        turmas = getIntent().getStringArrayListExtra("turmas");

        for (String idTurma: turmas) {
            popularRecyclerView(idTurma);
        }
    }

    private void popularRecyclerView(String idTurma) {
        Query ref = firestore.collection("turmas").whereEqualTo("id", idTurma);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaTurma(task);
                }
            }
        });
    }

    private void popularListaTurma(Task<QuerySnapshot> task) {
        List<Turma> turmaList = Objects.requireNonNull(task.getResult().toObjects(Turma.class));
        Turma t = turmaList.get(0);

        listaTurmas.add(t);

        adapter = new ProfessorTurmaAdapter(this, listaTurmas);

        recyclerViewTurma.setHasFixedSize(true);
        recyclerViewTurma.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurma.setAdapter(adapter);

        adapter.notifyItemInserted(turmas.size()-1);
    }

    @Override
    public void onItemClick(Turma turma, int posicao) {

    }
}