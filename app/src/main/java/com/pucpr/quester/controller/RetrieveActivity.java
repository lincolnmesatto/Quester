package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Arquivo;
import com.pucpr.quester.model.Disciplina;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RetrieveActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    ListView listView;
    List<Arquivo> arquivos;

    String idAluno;
    String idInstituicao;
    String idTurma;
    String idDisciplina;

    ArrayList<String> turmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);

        setTitle("Material de apoio");

        listView = findViewById(R.id.listPdfDownload);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Arquivo arquivo = arquivos.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(arquivo.getUrl()));
                startActivity(intent);
            }
        });
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

        for (int i = 0; i < filesNames.length; i++){
            filesNames[i] = arquivos.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, filesNames){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLACK);
                textView.setTextSize(20);

                return view;
            }
        };

        listView.setAdapter(adapter);
    }
}