package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questao;
import com.pucpr.quester.model.Questionario;
import com.pucpr.quester.model.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CadastrarQuestionarioActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> disciplinas;
    List<String> nomeDisciplinas;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_questionario);

        setTitle("Incluir Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        spinner = (Spinner) findViewById(R.id.spinnerDisciplina);

        disciplinas = getIntent().getStringArrayListExtra("disciplinas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }

        nomeDisciplinas = new ArrayList<>();

        for (String disc : disciplinas) {
            popularListaDisciplina(disc);
        }

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

        //spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeDisciplinas);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void adicionarQuestionario(View view){
//        if (id.equals("none")) {
            DocumentReference ref = firestore.collection("questionarios").document();
//            id = ref.getId();
//        }

//        if(mAwesomeValidation.validate()){
        List<Questao> questoes = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            List<Alternativa> alternativas = new ArrayList<>();
            for (int j = 0; j < 6; j++){
                alternativas.add(new Alternativa("Alternativa "+j, j == 0 ? 1 : 0));
            }

            questoes.add(new Questao("Enunciado "+i, alternativas));
        }

        Questionario questionario = new Questionario(ref.getId(), "TYdH5ouey2Qe2BFE1rB1", idTurma, idProfessor, questoes, 150.0);

        firestore.collection("questionarios").document(ref.getId()).set(questionario);
//        }else {
//            Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
//        }
    }
}