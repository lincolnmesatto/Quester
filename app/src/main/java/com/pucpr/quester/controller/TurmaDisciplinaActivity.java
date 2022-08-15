package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Instituicao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TurmaDisciplinaActivity extends AppCompatActivity implements TurmaDisciplinaAdapter.OnListItemClick, TurmaDisciplina2Adapter.OnListItemClick {

    FirebaseFirestore firestore;

    RecyclerView recyclerViewTurmaDisciplina;
    RecyclerView recyclerViewTurmaDisciplina2;

    TurmaDisciplinaAdapter turmaDisciplinaAdapter;
    TurmaDisciplina2Adapter turmaDisciplina2Adapter;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    String redirecionar;
    String idDisciplinaBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma_disciplina);

        firestore = FirebaseFirestore.getInstance();

        recyclerViewTurmaDisciplina = findViewById(R.id.turmaDiscRV1);
        recyclerViewTurmaDisciplina2 = findViewById(R.id.turmaDiscRV2);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
            redirecionar = extras.getString("redirecionar");
            idDisciplinaBack = extras.getString("idDisciplinaBack");

            if(redirecionar == null)
                redirecionar = "none";
            if(idDisciplinaBack == null)
                idDisciplinaBack = "none";
        }

        if(!redirecionar.equals("none")){
            if(redirecionar.equals("professor")){
                Intent i = new Intent(TurmaDisciplinaActivity.this, TurmaProfessorActivity.class);
                i.putExtra("id", id);
                i.putExtra("id_instituicao", idInstituicao);
                i.putExtra("nome_instituicao", nomeInstituicao);
                i.putExtra("id_disciplina", idDisciplinaBack);
                startActivity(i);
            }
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
        itemTouchHelper.attachToRecyclerView(recyclerViewTurmaDisciplina);

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
        itemTouchHelper2.attachToRecyclerView(recyclerViewTurmaDisciplina2);

    }

    public void popularRecyclerView(){
        CollectionReference ref = firestore.collection("disciplinas");

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularListaDisciplina(task);
                }
            }
        });
    }
    
    void popularListaDisciplina(Task<QuerySnapshot> t){
        List<Disciplina> ds = Objects.requireNonNull(t.getResult().toObjects(Disciplina.class));

        List<Disciplina> temp = new ArrayList<>();

        for (Disciplina d: ds) {
            boolean add = true;
            for (String idTurma: d.getTurmas()) {
                if(idTurma.equals(id)){
                    add = false;
                    break;
                }
            }

            if(add)
                temp.add(d);
        }

        turmaDisciplinaAdapter = new TurmaDisciplinaAdapter(this, temp);

        recyclerViewTurmaDisciplina.setHasFixedSize(true);
        recyclerViewTurmaDisciplina.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaDisciplina.setAdapter(turmaDisciplinaAdapter);
    }

    public void popularRecyclerView2(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("disciplinas").whereArrayContains("turmas", id);
        FirestorePagingOptions<Disciplina> options = new FirestorePagingOptions.Builder<Disciplina>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Disciplina>() {
                    @NonNull
                    @Override
                    public Disciplina parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Disciplina d = snapshot.toObject(Disciplina.class);
                        d.setId(snapshot.getId());
                        return d;
                    }
                })
                .build();

        turmaDisciplina2Adapter = new TurmaDisciplina2Adapter(options, this);

        recyclerViewTurmaDisciplina2.setHasFixedSize(true);
        recyclerViewTurmaDisciplina2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurmaDisciplina2.setAdapter(turmaDisciplina2Adapter);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Intent i = new Intent(TurmaDisciplinaActivity.this, TurmaProfessorActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        Disciplina d = snapshot.toObject(Disciplina.class);
        i.putExtra("id_disciplina", d.getId());
        startActivity(i);
    }

    @Override
    public void onItemClick(Disciplina disciplina, int posicao) {
        disciplina.getTurmas().add(id);

        firestore.collection("disciplinas").
                document(disciplina.getId()).set(disciplina);

        popularRecyclerView2();
        popularRecyclerView();

        turmaDisciplinaAdapter.notifyItemRemoved(posicao);
        voltaAnterior();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TurmaDisciplinaActivity.this, TurmaDerivadoActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    public void voltaAnterior() {
        Intent i = new Intent(TurmaDisciplinaActivity.this, TurmaDerivadoActivity.class);
        i.putExtra("id", id);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        i.putExtra("redirecionar", "disciplina");
        startActivity(i);
    }
}