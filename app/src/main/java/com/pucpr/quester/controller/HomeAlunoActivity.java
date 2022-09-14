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
import com.pucpr.quester.controller.adapter.InstituicaoHomeAdapter;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Instituicao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeAlunoActivity extends AppCompatActivity implements InstituicaoHomeAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    InstituicaoHomeAdapter adapter;

    List<Instituicao> instituicoes;
    List<Aluno> alunos;

    ArrayList<String> turmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_aluno);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setTitle("Home Aluno");

        recyclerView = findViewById(R.id.recyclerViewHomeAluno);

        instituicoes = new ArrayList<>();
        alunos = new ArrayList<>();
        turmas = new ArrayList<>();

        popularRecyclerView();
    }

    public void popularRecyclerView(){
        Query ref = firestore.collection("alunos").whereEqualTo("idUsuario", firebaseUser.getUid());

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaAluno(task);
                }
            }
        });
    }

    void popularListaAluno(Task<QuerySnapshot> t){
        List<Aluno> lista = Objects.requireNonNull(t.getResult().toObjects(Aluno.class));

        for (Aluno aluno: lista) {
            Query ref = firestore.collection("instituicoes").whereEqualTo("id", aluno.getIdInsituicao());

            Task<QuerySnapshot> task = ref.get();

            task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        pouplarInstituicao(aluno, task);
                    }
                }
            });
        }
    }

    void pouplarInstituicao(Aluno a, Task<QuerySnapshot> t){
        List<Instituicao> instituicaoList = Objects.requireNonNull(t.getResult().toObjects(Instituicao.class));
        Instituicao i = instituicaoList.get(0);

        instituicoes.add(i);
        alunos.add(a);

        adapter = new InstituicaoHomeAdapter(this, instituicoes);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(instituicoes.size()-1);
    }

    @Override
    public void onBackPressed() {
        //firebaseAuth.signOut();
        Intent i = new Intent(HomeAlunoActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(Instituicao instituicao, int posicao) {
        String id = "";
        for (Aluno a : alunos) {
            if(a.getIdInsituicao().equals(instituicao.getId())){
                for (String s: a.getTurmas()) {
                    if(!s.equals("-"))
                        turmas.add(s);
                }

                id = a.getId();
            }
        }


        Intent intent = new Intent(HomeAlunoActivity.this, AlunoInstituicaoDerivadoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", instituicao.getId());
        intent.putExtra("idAluno", id);
        startActivity(intent);
    }
}