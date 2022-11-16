package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.QuestionarioListAdapter;
import com.pucpr.quester.controller.adapter.RetrieveAdapter;
import com.pucpr.quester.model.Arquivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RetrieveActivity extends AppCompatActivity implements RetrieveAdapter.OnListItemClick {

    FirebaseFirestore firestore;

    RecyclerView recyclerView;
    List<Arquivo> arquivos;

    String idAluno;
    String idInstituicao;
    String idTurma;
    String idDisciplina;

    ArrayList<String> turmas;

    RetrieveAdapter retrieveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);

        setTitle("Material de apoio");

        recyclerView = findViewById(R.id.recyclerViewRetrieve);

        firestore = FirebaseFirestore.getInstance();
        arquivos = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
            idDisciplina = extras.getString("idDisciplina");

            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        retrieveFiles();

    }

    private void retrieveFiles() {
        Query ref = firestore.collection("arquivos").whereEqualTo("idDisciplina", idDisciplina).whereEqualTo("idTurma", idTurma);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarArquivo(task);
                }
            }
        });
    }

    private void buscarArquivo(Task<QuerySnapshot> task){
        arquivos = Objects.requireNonNull(task.getResult().toObjects(Arquivo.class));

        String[] filesNames = new String[arquivos.size()];

        retrieveAdapter = new RetrieveAdapter(this, arquivos);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(retrieveAdapter);

    }

    @Override
    public void onItemClick(Arquivo arquivo, int posicao) {

        Arquivo a = arquivos.get(posicao);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        intent.setData(Uri.parse(arquivo.getUrl()));
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RetrieveActivity.this, DownloadActivity.class);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        intent.putExtra("idTurma", idTurma);
        intent.putStringArrayListExtra("turmas", turmas);
        startActivity(intent);
    }
}