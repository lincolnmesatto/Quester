package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class CadastrarQuestionarioActivity extends AppCompatActivity implements QuestionarioAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    QuestionarioAdapter questionarioAdapter;

    RecyclerView recyclerViewQuestoes;
    EditText editTextTitulo;

    ArrayList<String> disciplinas;
    List<String> nomeDisciplinas;
    List<Disciplina> disciplinaList;
    List<Questao> questaoList;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    Spinner spinner;
    Spinner spinnerPontuacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_questionario);

        setTitle("Incluir Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        spinner = (Spinner) findViewById(R.id.spinnerDisciplina);
        spinnerPontuacao = (Spinner) findViewById(R.id.spinnerTipoPontuacao);
        editTextTitulo = findViewById(R.id.editTextTitulo);
        recyclerViewQuestoes = findViewById(R.id.recyclerViewQuestoes);

        ArrayAdapter<CharSequence> adapterPontuacao = ArrayAdapter.createFromResource(CadastrarQuestionarioActivity.this, R.array.pontuacao_type,
                android.R.layout.simple_spinner_item);
        adapterPontuacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPontuacao.setAdapter(adapterPontuacao);

        disciplinas = getIntent().getStringArrayListExtra("disciplinas");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }

        nomeDisciplinas = new ArrayList<>();
        nomeDisciplinas.add("Selecione");
        disciplinaList = new ArrayList<>();

        for (String disc : disciplinas) {
            popularListaDisciplina(disc);
        }

        questaoList = new ArrayList<>();
        Questao q = new Questao();
        q.setAlternativas(new ArrayList<>());
        q.getAlternativas().add(new Alternativa());
        questaoList.add(q);

        questionarioAdapter = new QuestionarioAdapter(this, questaoList);

        recyclerViewQuestoes.setHasFixedSize(true);
        recyclerViewQuestoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestoes.setAdapter(questionarioAdapter);

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
        questaoList.size();
        List<Questao> questoes = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            List<Alternativa> alternativas = new ArrayList<>();
            for (int j = 0; j < 5; j++){
                alternativas.add(new Alternativa("Alternativa "+j, j == 0 ? 1 : 0));
            }

            questoes.add(new Questao("Enunciado "+i, alternativas));
        }

        Questionario questionario = new Questionario(ref.getId(), "teste", 1, disciplinaList.get(0).getId(), idTurma, idProfessor, questoes, 150.0);

        firestore.collection("questionarios").document(ref.getId()).set(questionario);
//        }else {
//            Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onItemClick(Questao questao, int posicao) {
        questao.getAlternativas().add(new Alternativa());
        questionarioAdapter.notifyItemInserted(questaoList.size()-1);
    }

    public void btnAddQuestaoClicked(View view){
        Questao q = new Questao();
        q.setAlternativas(new ArrayList<>());
        q.getAlternativas().add(new Alternativa());
        questaoList.add(q);

        questionarioAdapter.notifyItemInserted(questaoList.size()-1);
    }
}