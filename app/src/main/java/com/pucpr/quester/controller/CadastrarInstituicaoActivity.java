package com.pucpr.quester.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;

public class CadastrarInstituicaoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    EditText editTextNome;
    EditText editTextTelefone;
    EditText editTextUF;
    EditText editTextCidade;
    EditText editTextBairro;
    EditText editTextLogradouro;
    EditText editTextComplemento;
    EditText editTextNumero;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_instituicao);

        setTitle("Cadastrar Instituição");

        editTextNome = findViewById(R.id.editTextNome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextUF = findViewById(R.id.editTextUF);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextLogradouro = findViewById(R.id.editTextLogradouro);
        editTextComplemento = findViewById(R.id.editTextComplemento);
        editTextNumero = findViewById(R.id.editTextNumero);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");

            DocumentReference docRef = firestore.collection("instituicoes").document(id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Instituicao i = documentSnapshot.toObject(Instituicao.class);

                    editTextNome.setText(i.getNome());
                    editTextTelefone.setText(i.getTelefone());
                    editTextUF.setText(i.getEstado());
                    editTextCidade.setText(i.getCidade());
                    editTextBairro.setText(i.getBairro());
                    editTextLogradouro.setText(i.getLogradouro());
                    editTextComplemento.setText(i.getComplemento());
                    editTextNumero.setText(i.getNumero());
                }
            });

        }else{
            id = "none";
        }
    }

    public void buttonSalvarClicked(View view) {
        if(id == "none") {
            DocumentReference ref = firestore.collection("instituicoes").document();
            id = ref.getId();
        }
        Instituicao instituicao = new Instituicao(id, editTextNome.getText().toString(), editTextUF.getText().toString(),
                editTextCidade.getText().toString(), editTextBairro.getText().toString(),
                editTextLogradouro.getText().toString(), editTextNumero.getText().toString(),
                editTextComplemento.getText().toString(), editTextTelefone.getText().toString());

        criarInstituicao(instituicao);
    }

    void criarInstituicao(Instituicao instituicao) {
        firestore.collection("instituicoes").
                document(instituicao.getId()).set(instituicao);

        firestore.collection("instituicoes").
                document(instituicao.getId()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Instituicao instituicao = value.toObject(Instituicao.class);
                            Log.d("Firestore",instituicao.getNome());
                        }
                    }
                });

        Intent i = new Intent(CadastrarInstituicaoActivity.this, HomeActivity.class);
        startActivity(i);
    }
}