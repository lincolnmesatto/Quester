package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.pucpr.quester.model.TurmaDisciplinaModel;
import com.pucpr.quester.model.Usuario;
import com.pucpr.quester.model.UsuarioProfessorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeProfessorActivity extends AppCompatActivity implements InstituicaoHomeAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    InstituicaoHomeAdapter adapter;

    List<Instituicao> instituicoes;
    List<Professor> professores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_professor);

        recyclerView = findViewById(R.id.recyclerViewHomeProf);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setTitle("Home Professor");

        instituicoes = new ArrayList<>();
        professores = new ArrayList<>();

        popularRecyclerView();
    }

    public void popularRecyclerView(){
        Query ref = firestore.collection("professores").whereEqualTo("idUsuario", firebaseUser.getUid());

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaProfessor(task);
                }
            }
        });
    }

    void popularListaProfessor(Task<QuerySnapshot> t){
        List<Professor> professores = Objects.requireNonNull(t.getResult().toObjects(Professor.class));

        for (Professor professor: professores) {
            Query ref = firestore.collection("instituicoes").whereEqualTo("id", professor.getIdInsituicao());

            Task<QuerySnapshot> task = ref.get();

            task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        pouplarInstituicao(professor, task);
                    }
                }
            });
        }
    }

    void pouplarInstituicao(Professor p, Task<QuerySnapshot> t){
        List<Instituicao> instituicaoList = Objects.requireNonNull(t.getResult().toObjects(Instituicao.class));
        Instituicao i = instituicaoList.get(0);

        instituicoes.add(i);
        professores.add(p);

        adapter = new InstituicaoHomeAdapter(this, instituicoes);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.notifyItemInserted(instituicoes.size()-1);
    }

    @Override
    public void onBackPressed() {
        //firebaseAuth.signOut();
        Intent i = new Intent(HomeProfessorActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(Instituicao instituicao, int posicao) {
        List<TurmaDisciplinaModel> tdm = new ArrayList<>();
        for (Professor p : professores) {
            if(p.getIdInsituicao().equals(instituicao.getId())){
                tdm.addAll(p.getTdm());
            }
        }

        List<String> turmas = new ArrayList<String>();
        List<String> discs = new ArrayList<>();

        turmas.add(tdm.get(0).getIdTurma());
        for (TurmaDisciplinaModel td: tdm) {
            if(!turmas.contains(td.getIdTurma())){
                turmas.add(td.getIdTurma());
            }
            discs.add(td.getIdTurma()+";"+td.getIdDisciplina());
        }

        Intent intent = new Intent(HomeProfessorActivity.this, ProfessorTurmaActivity.class);
        intent.putStringArrayListExtra("turmas", (ArrayList<String>) turmas);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) discs);
        intent.putExtra("idInstituicao", instituicao.getId());
        intent.putExtra("idProfessor", professores.get(0).getId());
        startActivity(intent);
    }
}