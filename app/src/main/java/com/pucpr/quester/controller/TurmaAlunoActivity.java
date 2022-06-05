package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Usuario;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TurmaAlunoActivity extends AppCompatActivity implements TurmaAlunoAdapter.OnListItemClick{

    FirebaseFirestore firestore;

    RecyclerView recyclerViewTurmaAluno;
    RecyclerView recyclerViewTurmaAluno2;

    TurmaAlunoAdapter turmaAlunoAdapter;
    TurmaAluno2Adapter turmaAluno2Adapter;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    List<UsuarioAlunoModel> alunos;
    List<UsuarioAlunoModel> alunos2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma_aluno);

        firestore = FirebaseFirestore.getInstance();

        recyclerViewTurmaAluno = findViewById(R.id.turmaAlunoRV1);
        recyclerViewTurmaAluno2 = findViewById(R.id.turmaAlunoRV2);

        alunos = new ArrayList<>();
        alunos2 = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
        }

        popularRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){

                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerViewTurmaAluno);

        popularRecyclerView2();

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){

                    }
                }
        );
        itemTouchHelper2.attachToRecyclerView(recyclerViewTurmaAluno2);
    }

    public void popularRecyclerView(){
        Query ref = firestore.collection("alunos").whereEqualTo("idInsituicao", idInstituicao);

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
        List<Aluno> alunos = Objects.requireNonNull(t.getResult().toObjects(Aluno.class));
        List<UsuarioAlunoModel> temp = new ArrayList<>();

        for (Aluno aluno: alunos) {
            boolean add = true;
            for (String idTurma: aluno.getTurmas()) {
                if(idTurma.equals(id)){
                    add = false;
                    break;
                }
            }
            if(add){
                Query ref = firestore.collection("usuarios").whereEqualTo("idUsuario", aluno.getIdUsuario());

                Task<QuerySnapshot> task = ref.get();

                task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            pouplarUsuario(aluno, task);
                        }
                    }
                });
            }
        }

        turmaAlunoAdapter = new TurmaAlunoAdapter(this, temp);

        recyclerViewTurmaAluno.setHasFixedSize(true);
        recyclerViewTurmaAluno.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaAluno.setAdapter(turmaAlunoAdapter);
    }

    void pouplarUsuario(Aluno a, Task<QuerySnapshot> t){
        List<Usuario> usuarios = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));
        Usuario u = usuarios.get(0);

        UsuarioAlunoModel ua = new UsuarioAlunoModel(a, u);

        alunos.add(ua);

        turmaAlunoAdapter = new TurmaAlunoAdapter(this, alunos);

        recyclerViewTurmaAluno.setHasFixedSize(true);
        recyclerViewTurmaAluno.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaAluno.setAdapter(turmaAlunoAdapter);

        turmaAlunoAdapter.notifyItemInserted(alunos.size()-1);
    }

    public void popularRecyclerView2(){
        Query ref = firestore.collection("alunos").whereEqualTo("idInsituicao", idInstituicao).whereArrayContains("turmas", id);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaAluno2(task);
                }
            }
        });
    }

    void popularListaAluno2(Task<QuerySnapshot> t){
        List<Aluno> alunos = Objects.requireNonNull(t.getResult().toObjects(Aluno.class));
        List<UsuarioAlunoModel> temp = new ArrayList<>();

        for (Aluno aluno: alunos) {
            Query ref = firestore.collection("usuarios").whereEqualTo("idUsuario", aluno.getIdUsuario());

            Task<QuerySnapshot> task = ref.get();

            task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        pouplarUsuario2(aluno, task);
                    }
                }
            });
        }

        turmaAluno2Adapter = new TurmaAluno2Adapter(temp);

        recyclerViewTurmaAluno2.setHasFixedSize(true);
        recyclerViewTurmaAluno2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaAluno2.setAdapter(turmaAluno2Adapter);
    }

    void pouplarUsuario2(Aluno a, Task<QuerySnapshot> t){
        List<Usuario> usuarios = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));
        Usuario u = usuarios.get(0);
        UsuarioAlunoModel ua = new UsuarioAlunoModel(a, u);

        alunos2.add(ua);

        turmaAluno2Adapter = new TurmaAluno2Adapter(alunos2);

        recyclerViewTurmaAluno2.setHasFixedSize(true);
        recyclerViewTurmaAluno2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaAluno2.setAdapter(turmaAluno2Adapter);

        turmaAluno2Adapter.notifyItemInserted(alunos2.size()-1);
    }

    @Override
    public void onItemClick(Aluno aluno, int posicao) {
        aluno.getTurmas().add(id);

        firestore.collection("alunos").
                document(aluno.getId()).set(aluno);

        popularRecyclerView2();
        popularRecyclerView();

        turmaAlunoAdapter.notifyItemRemoved(posicao);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TurmaAlunoActivity.this, TurmaDerivadoActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}