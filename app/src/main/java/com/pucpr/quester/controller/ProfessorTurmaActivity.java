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
import com.pucpr.quester.controller.adapter.ProfessorTurmaAdapter;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfessorTurmaActivity extends AppCompatActivity implements ProfessorTurmaAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> turmas;
    ArrayList<String> disciplinas;

    List<Turma> listaTurmas;

    RecyclerView recyclerViewTurma;
    ProfessorTurmaAdapter adapter;

    String idProfessor;
    String idInstituicao;

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
        disciplinas = getIntent().getStringArrayListExtra("disciplinas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
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

        adapter = new ProfessorTurmaAdapter(this, listaTurmas);

        recyclerViewTurma.setHasFixedSize(true);
        recyclerViewTurma.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurma.setAdapter(adapter);

        adapter.notifyItemInserted(turmas.size()-1);
    }

    @Override
    public void onItemClick(Turma turma, int posicao) {
        List<String> discs = new ArrayList<>();
        for (String d: disciplinas) {
            String[] partes = d.split(";");
            String idTurma = partes[0];
            String idDisciplina = partes[1];

            if(turma.getId().equals(idTurma) && !idDisciplina.equals("none") ){
                discs.add(partes[1]);
            }
        }

        Intent intent = new Intent(ProfessorTurmaActivity.this, QuestionarioActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) discs);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idProfessor", idProfessor);
        intent.putExtra("idTurma", turma.getId());
        startActivity(intent);
    }
}