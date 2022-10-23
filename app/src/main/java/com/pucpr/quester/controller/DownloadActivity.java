package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.TurmaDisciplina2Adapter;
import com.pucpr.quester.controller.adapter.TurmaDisciplinaAdapter;
import com.pucpr.quester.model.Disciplina;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements TurmaDisciplina2Adapter.OnListItemClick {

    FirebaseFirestore firestore;

    RecyclerView recyclerView;

    String idAluno;
    String idInstituicao;
    String idTurma;

    ArrayList<String> turmas;

    TurmaDisciplina2Adapter turmaDisciplina2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        recyclerView = findViewById(R.id.recyclerViewDisciplinaDownload);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");

            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        popularRecyclerView2();
    }

    public void retrievePdf(View view) {
        Intent intent = new Intent(DownloadActivity.this, RetrieveActivity.class);
//        intent.putStringArrayListExtra("disciplinas", disciplinas);
//        intent.putExtra("idInstituicao", idInstituicao);
//        intent.putExtra("idProfessor", idProfessor);
//        intent.putExtra("idTurma", idTurma);
//        intent.putExtra("idQuestionario", "none");
        startActivity(intent);
    }

    public void popularRecyclerView2(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("disciplinas").whereArrayContains("turmas", idTurma);
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(turmaDisciplina2Adapter);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Intent intent = new Intent(DownloadActivity.this, RetrieveActivity.class);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        intent.putExtra("idTurma", idTurma);
        intent.putExtra("idDisciplina", snapshot.getId());
        intent.putStringArrayListExtra("turmas", turmas);
        startActivity(intent);
    }
}