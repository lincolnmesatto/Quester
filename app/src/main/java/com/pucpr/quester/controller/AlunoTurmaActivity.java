package com.pucpr.quester.controller;

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
import com.pucpr.quester.controller.adapter.AlunoTurmaAdapter;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlunoTurmaActivity extends AppCompatActivity implements AlunoTurmaAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> turmas;
    List<Turma> listaTurmas;

    RecyclerView recyclerViewTurma;
    AlunoTurmaAdapter adapter;

    String idAluno;
    String idInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_turma);

        setTitle("Turmas");
        recyclerViewTurma = findViewById(R.id.recyclerViewAlunoTurmaList);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        listaTurmas = new ArrayList<>();

        turmas = getIntent().getStringArrayListExtra("turmas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
        }

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

        adapter = new AlunoTurmaAdapter(this, listaTurmas);

        recyclerViewTurma.setHasFixedSize(true);
        recyclerViewTurma.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurma.setAdapter(adapter);

        adapter.notifyItemInserted(turmas.size()-1);
    }

    @Override
    public void onItemClick(Turma turma, int posicao) {
        Intent intent = new Intent(AlunoTurmaActivity.this, QuestionarioAlunoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        intent.putExtra("idTurma", turma.getId());
        startActivity(intent);
    }
}