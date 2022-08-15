package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.TurmaDisciplinaModel;
import com.pucpr.quester.model.Usuario;
import com.pucpr.quester.model.UsuarioAlunoModel;
import com.pucpr.quester.model.UsuarioProfessorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TurmaProfessorActivity extends AppCompatActivity implements TurmaProfessorAdapter.OnListItemClick {

    FirebaseFirestore firestore;

    RecyclerView recyclerViewTurmaProfessor;
    RecyclerView recyclerViewTurmaProfessor2;

    TurmaProfessorAdapter turmaProfessorAdapter;
    TurmaProfessor2Adapter turmaProfessor2Adapter;

    String id;
    String idInstituicao;
    String nomeInstituicao;
    String idDisciplina;

    TurmaDisciplinaModel tdm;

    List<UsuarioProfessorModel> professores;
    List<UsuarioProfessorModel> professores2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma_professor);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
            idDisciplina = extras.getString("id_disciplina");
        }

        tdm = new TurmaDisciplinaModel(id, idDisciplina);

        recyclerViewTurmaProfessor = findViewById(R.id.turmaProfRV1);
        recyclerViewTurmaProfessor2 = findViewById(R.id.turmaProfRV2);

        professores = new ArrayList<>();
        professores2 = new ArrayList<>();

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
        itemTouchHelper.attachToRecyclerView(recyclerViewTurmaProfessor);

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
        itemTouchHelper2.attachToRecyclerView(recyclerViewTurmaProfessor2);
    }

    public void popularRecyclerView(){
        Query ref = firestore.collection("professores").whereEqualTo("idInsituicao", idInstituicao);

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
        List<UsuarioProfessorModel> temp = new ArrayList<>();

        for (Professor professor: professores) {
            boolean add = true;
            for (TurmaDisciplinaModel tdm: professor.getTdm()) {
                if(tdm.getIdTurma().equals(id) && tdm.getIdDisciplina().equals(idDisciplina)){
                    add = false;
                    break;
                }
            }
            if(add){
                Query ref = firestore.collection("usuarios").whereEqualTo("idUsuario", professor.getIdUsuario());

                Task<QuerySnapshot> task = ref.get();

                task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            pouplarUsuario(professor, task);
                        }
                    }
                });
            }
        }

        turmaProfessorAdapter = new TurmaProfessorAdapter(this, temp);

        recyclerViewTurmaProfessor.setHasFixedSize(true);
        recyclerViewTurmaProfessor.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaProfessor.setAdapter(turmaProfessorAdapter);
    }

    void pouplarUsuario(Professor p, Task<QuerySnapshot> t){
        List<Usuario> usuarios = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));
        Usuario u = usuarios.get(0);

        UsuarioProfessorModel ua = new UsuarioProfessorModel(p, u);

        professores.add(ua);

        turmaProfessorAdapter = new TurmaProfessorAdapter(this, professores);

        recyclerViewTurmaProfessor.setHasFixedSize(true);
        recyclerViewTurmaProfessor.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaProfessor.setAdapter(turmaProfessorAdapter);

        turmaProfessorAdapter.notifyItemInserted(professores.size()-1);
    }

    public void popularRecyclerView2(){
        Query query =  firestore.collection("professores").whereEqualTo("idInsituicao", idInstituicao).whereArrayContains("tdm", tdm);

        Task<QuerySnapshot> t = query.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaProfessor2(task);
                }
            }
        });
    }

    void popularListaProfessor2(Task<QuerySnapshot> t){
        List<Professor> ps = Objects.requireNonNull(t.getResult().toObjects(Professor.class));
        List<UsuarioProfessorModel> temp = new ArrayList<>();

        for (Professor professor: ps) {
            Query ref = firestore.collection("usuarios").whereEqualTo("idUsuario", professor.getIdUsuario());

            Task<QuerySnapshot> task = ref.get();

            task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        pouplarUsuario2(professor, task);
                    }
                }
            });
        }

        turmaProfessor2Adapter = new TurmaProfessor2Adapter(temp);

        recyclerViewTurmaProfessor2.setHasFixedSize(true);
        recyclerViewTurmaProfessor2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaProfessor2.setAdapter(turmaProfessor2Adapter);
    }

    void pouplarUsuario2(Professor p, Task<QuerySnapshot> t){
        List<Usuario> usuarios = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));
        Usuario u = usuarios.get(0);
        UsuarioProfessorModel ua = new UsuarioProfessorModel(p, u);

        professores2.add(ua);

        turmaProfessor2Adapter = new TurmaProfessor2Adapter(professores2);

        recyclerViewTurmaProfessor2.setHasFixedSize(true);
        recyclerViewTurmaProfessor2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaProfessor2.setAdapter(turmaProfessor2Adapter);

        turmaProfessor2Adapter.notifyItemInserted(professores2.size()-1);
    }

    @Override
    public void onItemClick(Professor professor, int posicao) {
        professor.getTdm().add(new TurmaDisciplinaModel(id, idDisciplina));

        firestore.collection("professores").
                document(professor.getId()).set(professor);
        voltaAnterior();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TurmaProfessorActivity.this, TurmaDisciplinaActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    public void voltaAnterior() {
        Intent i = new Intent(TurmaProfessorActivity.this, TurmaDisciplinaActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        i.putExtra("redirecionar", "professor");
        i.putExtra("id_disciplina", idDisciplina);
        startActivity(i);
    }
}