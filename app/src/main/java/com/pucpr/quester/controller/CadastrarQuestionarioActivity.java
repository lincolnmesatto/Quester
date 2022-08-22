package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.pucpr.quester.model.DataModel;
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
    EditText editTextXp;
    Spinner spinner;
    Spinner spinnerPontuacao;

    ArrayList<String> disciplinas;
    List<String> nomeDisciplinas;
    List<Disciplina> disciplinaList;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_questionario);

        setTitle("Incluir Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DataModel.getInstance().setContext(CadastrarQuestionarioActivity.this);

        spinner = (Spinner) findViewById(R.id.spinnerDisciplina);
        spinnerPontuacao = (Spinner) findViewById(R.id.spinnerTipoPontuacao);
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextXp = findViewById(R.id.editTextXp);
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

        DataModel.getInstance().setQuestoesDataModel(new ArrayList<>());
        Questao q = new Questao();
        q.setAlternativas(new ArrayList<>());
        for(int i=0; i<=4; i++){
            q.getAlternativas().add(new Alternativa());
        }
        DataModel.getInstance().getQuestoesDataModel().add(q);

        questionarioAdapter = new QuestionarioAdapter(this, DataModel.getInstance().getQuestoesDataModel());

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

        DataModel.getInstance().getQuestoesDataModel().get(0).getEnunciado();
//        List<Questao> questoes = new ArrayList<>();
//        for (Questao q : DataModel.getInstance().getQuestoesDataModel()){
//            List<Alternativa> alternativas = new ArrayList<>();
//            for (int j = 0; j < 5; j++){
//                alternativas.add(new Alternativa("Alternativa "+j, j == 0 ? 1 : 0));
//            }
//
//            questoes.add(new Questao(q.getEnunciado(), alternativas));
//        }

        Questionario questionario = new Questionario(ref.getId(), editTextTitulo.getText().toString(), spinnerPontuacao.getSelectedItemPosition(), disciplinas.get(spinner.getSelectedItemPosition()-1),
                idTurma, idProfessor, DataModel.getInstance().getQuestoesDataModel(), Double.valueOf(editTextXp.getText().toString()));

        firestore.collection("questionarios").document(ref.getId()).set(questionario);
//        }else {
//            Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
//        }

        Intent intent = new Intent(CadastrarQuestionarioActivity.this, QuestionarioActivity.class);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) disciplinas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idProfessor", idProfessor);
        intent.putExtra("idTurma", idTurma);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Questao questao, int posicao) {

    }

    public void btnAddQuestaoClicked(View view){
        Questao q = new Questao();
        q.setAlternativas(new ArrayList<>());
        q.getAlternativas().add(new Alternativa());
        DataModel.getInstance().getQuestoesDataModel().add(q);

        questionarioAdapter.notifyItemInserted(DataModel.getInstance().getQuestoesDataModel().size()-1);
    }
}