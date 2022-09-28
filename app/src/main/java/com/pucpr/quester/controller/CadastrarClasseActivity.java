package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Classe;
import com.pucpr.quester.model.Disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CadastrarClasseActivity extends AppCompatActivity {

    private final String VERIFICAR = "Verificar";

    FirebaseFirestore firestore;

    String id;
    EditText editTextClasse;
    EditText editTextBonus;
    Spinner spinnerClasseDisciplina;

    List<Disciplina> disciplinas;

    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_classe);

        setTitle("Cadastrar Classe");

        editTextClasse = findViewById(R.id.editTextClasse);
        editTextBonus = findViewById(R.id.editTextBonus);
        spinnerClasseDisciplina = findViewById(R.id.spinnerClasseDisciplina);
        disciplinas = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");

            DocumentReference docRef = firestore.collection("classes").document(id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Classe c = documentSnapshot.toObject(Classe.class);

                    editTextClasse.setText(c.getNome());
                }
            });

        }else{
            id = "none";
        }
    }

    private void popularListaDisciplina(String disc) {
        Query ref = firestore.collection("disciplinas");

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

    private void buscarDisciplina(Task<QuerySnapshot> task) {
        disciplinas = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
        ArrayList<String> nomesDisciplina = new ArrayList<>();
        for (Disciplina d:disciplinas) {
            nomesDisciplina.add(d.getNome());
        }

//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesDisciplina);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerClasseDisciplina.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<CharSequence> adapterPontuacao = ArrayAdapter.createFromResource(CadastrarClasseActivity.this, R.array.pontuacao_type,
                android.R.layout.simple_spinner_item);
        adapterPontuacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasseDisciplina.setAdapter(adapterPontuacao);
    }

//    public void buttonSalvarClicked(View view) {
//
//        if (id == "none") {
//            DocumentReference ref = firestore.collection("disciplinas").document();
//            id = ref.getId();
//        }
//
//        Classe classe = new Classe(id, editTextClasse.getText().toString(),editTextBonus,spinnerClasseDisciplina);
//
//        //if(mAwesomeValidation.validate()){
//        criarClasse(classe);
//                }else {
//                    Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
//                }
//    }
}