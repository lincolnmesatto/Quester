package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questionario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CadastrarClasseActivity extends AppCompatActivity {
    FirebaseFirestore firestore;

    String id;
    EditText editTextClasse;
    EditText editTextBonus;
    Spinner spinnerClasseDisciplina;

    List<Disciplina> disciplinas;
    List<Disciplina> disciplinasList;
    List<Classe> classeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_classe);

        setTitle("Cadastrar Classe");

        editTextClasse = findViewById(R.id.editTextClasse);
        editTextBonus = findViewById(R.id.editTextBonus);
        spinnerClasseDisciplina = findViewById(R.id.spinnerClasseDisciplina);

        disciplinas = new ArrayList<>();
        disciplinasList = new ArrayList<>();
        classeList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
        }else{
            id = "none";
        }

        popularListaClasse();
        popularListaDisciplina();

        if(!id.equals("none"))
            popularClasse(id);
    }

    private void popularListaDisciplina() {
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
        nomesDisciplina.add("Disciplinas");

        if(!id.equals("none")){
            for (Disciplina d:disciplinas) {
                nomesDisciplina.add(d.getNome());
            }
            spinnerClasseDisciplina.setEnabled(false);
        }else{
            List<Disciplina> temp = new ArrayList<>();
            for (Disciplina d : disciplinas) {
                for (Classe c : classeList) {
                    if(c.getIdDisciplina().equals(d.getId()))
                        temp.add(d);
                }
            }

            disciplinas.removeAll(temp);

            for (Disciplina d:disciplinas) {
                nomesDisciplina.add(d.getNome());
            }
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesDisciplina);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasseDisciplina.setAdapter(spinnerArrayAdapter);

    }

    private void popularClasse(String id) {
        Query ref = firestore.collection("classes").whereEqualTo("id",id);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarClasse(task);
                }
            }
        });
    }

    private void buscarClasse(Task<QuerySnapshot> task) {
        List<Classe> classes = Objects.requireNonNull(task.getResult().toObjects(Classe.class));
        Classe c = classes.get(0);
        editTextClasse.setText(c.getNome());
        editTextBonus.setText(String.valueOf(c.getBonus()));

        for(int i = 0; i<=disciplinas.size();i++){
            if(disciplinas.get(i).getId().equals(c.getIdDisciplina())){
                spinnerClasseDisciplina.setSelection(i+1);
                spinnerClasseDisciplina.setEnabled(false);
                break;
            }
        }

    }

    public void buttonSalvarClicked(View view) {

        if (id == "none") {
            id = disciplinas.get(spinnerClasseDisciplina.getSelectedItemPosition()-1).getId();
        }

        Classe classe = new Classe(id, editTextClasse.getText().toString(),Integer.valueOf(editTextBonus.getText().toString()),disciplinas.get(spinnerClasseDisciplina.getSelectedItemPosition()-1).getId());
        criarClasse(classe);
    }
    public void criarClasse(Classe classe){
        firestore.collection("classes").document(id).set(classe);

        Intent intent = new Intent(CadastrarClasseActivity.this, ClasseActivity.class);
        startActivity(intent);
    }

    private void popularListaClasse() {
        Query ref = firestore.collection("classes");

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarListaClasse(task);
                }
            }
        });
    }

    private void buscarListaClasse(Task<QuerySnapshot> task) {
        classeList = Objects.requireNonNull(task.getResult().toObjects(Classe.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastrarClasseActivity.this, ClasseActivity.class);
        startActivity(intent);
    }
}