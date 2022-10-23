package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Arquivo;
import com.pucpr.quester.model.Disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UploadActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    StorageReference storageReference;

    EditText editText;
    Button btn;
    Spinner spinner;

    ArrayList<String> disciplinas;
    List<String> nomeDisciplinas;
    List<Disciplina> disciplinaList;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        editText = findViewById(R.id.editTextUpdload);
        btn = findViewById(R.id.btnUpload);
        spinner = findViewById(R.id.spinnerDisciplinaUpload);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        disciplinas = getIntent().getStringArrayListExtra("disciplinas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }

        nomeDisciplinas = new ArrayList<>();
        nomeDisciplinas.add("Disciplina");
        disciplinaList = new ArrayList<>();

        for (String disc : disciplinas) {
            popularListaDisciplina(disc);
        }

        btn.setEnabled(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });
    }

    private void selectPdf(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            btn.setEnabled(true);
            editText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDF(data.getData());
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando...");
        progressDialog.show();

        StorageReference reference = storageReference.child("upload"+System.currentTimeMillis()+".pdf");

        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                Arquivo arquivo = new Arquivo(editText.getText().toString(), uri.toString(), idTurma, disciplinas.get(spinner.getSelectedItemPosition()-1));

                DocumentReference ref = firestore.collection("disciplinas").document();
                String id = ref.getId();
                firestore.collection("arquivos").document(id).set(arquivo);

                Toast.makeText(UploadActivity.this, "Arquivo carregado com sucesso", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                progressDialog.setMessage("Carregando... "+(int) progress+"%");
            }
        });
    }

    private void popularListaDisciplina(String disc) {
        Query ref = firestore.collection("disciplinas").whereEqualTo("id", disc);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarDisciplina(task);
                }
            }
        });
    }

    private void buscarDisciplina(Task<QuerySnapshot> task){
        List<Disciplina> disciplinaList = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
        Disciplina d = disciplinaList.get(0);

        nomeDisciplinas.add(d.getNome());
        disciplinaList.add(d);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeDisciplinas);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}