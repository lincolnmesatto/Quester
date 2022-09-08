package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.QuestionarioAdapter;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questao;
import com.pucpr.quester.model.Questionario;

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
    String idQuestionario;

    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_questionario);

        setTitle("Incluir Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DataModel.getInstance().setContext(CadastrarQuestionarioActivity.this);

        spinner = findViewById(R.id.spinnerDisciplina);
        spinnerPontuacao = findViewById(R.id.spinnerTipoPontuacao);
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
            idQuestionario = extras.getString("idQuestionario");
        }

        nomeDisciplinas = new ArrayList<>();
        nomeDisciplinas.add("Disciplina");
        disciplinaList = new ArrayList<>();

        for (String disc : disciplinas) {
            popularListaDisciplina(disc);
        }

        DataModel.getInstance().setQuestoesDataModel(new ArrayList<>());
        if(idQuestionario.equals("none")){
            Questao q = new Questao();
            q.setAlternativas(new ArrayList<>());
            for(int i=0; i<=4; i++){
                q.getAlternativas().add(new Alternativa());
            }
            DataModel.getInstance().getQuestoesDataModel().add(q);
            DataModel.getInstance().setInsert(true);
        }else{
            DataModel.getInstance().setInsert(false);
            popularListaQuestionario(idQuestionario);
        }

        questionarioAdapter = new QuestionarioAdapter(this, DataModel.getInstance().getQuestoesDataModel());

        recyclerViewQuestoes.setHasFixedSize(true);
        recyclerViewQuestoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestoes.setAdapter(questionarioAdapter);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);
        mAwesomeValidation.addValidation(this, R.id.editTextTitulo, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextXp, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
    }

    private void popularListaQuestionario(String idQuestionario) {
        Query ref = firestore.collection("questionarios").whereEqualTo("id", idQuestionario);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarQuestionario(task);
                }
            }
        });
    }

    private void buscarQuestionario(Task<QuerySnapshot> task) {
        List<Questionario> qs = Objects.requireNonNull(task.getResult().toObjects(Questionario.class));
        Questionario q = qs.get(0);

        DataModel.getInstance().getQuestoesDataModel().addAll(q.getQuestoes());
        for(int i = 0; i<=disciplinas.size();i++){
            if(disciplinas.get(i).equals(q.getIdDisciplina())){
                spinner.setSelection(i+1);
                break;
            }
        }

        spinnerPontuacao.setSelection(q.getTipoPontuacao());
        editTextTitulo.setText(q.getTitulo());
        editTextXp.setText(String.valueOf(q.getXp()));

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

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeDisciplinas);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void adicionarQuestionario(View view){
        if (idQuestionario.equals("none")) {
            DocumentReference ref = firestore.collection("questionarios").document();
            idQuestionario = ref.getId();
        }

        if(mAwesomeValidation.validate()){
            if(validarSalvar()){
                Questionario questionario = new Questionario(idQuestionario, editTextTitulo.getText().toString(), spinnerPontuacao.getSelectedItemPosition(), disciplinas.get(spinner.getSelectedItemPosition()-1),
                        idTurma, idProfessor, DataModel.getInstance().getQuestoesDataModel(), Double.valueOf(editTextXp.getText().toString()));

                firestore.collection("questionarios").document(idQuestionario).set(questionario);

                Intent intent = new Intent(CadastrarQuestionarioActivity.this, QuestionarioActivity.class);
                intent.putStringArrayListExtra("disciplinas", disciplinas);
                intent.putExtra("idInstituicao", idInstituicao);
                intent.putExtra("idProfessor", idProfessor);
                intent.putExtra("idTurma", idTurma);
                startActivity(intent);
            }
        }else {
            Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarSalvar() {
        if(spinner.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), "Informe a disciplina",Toast.LENGTH_LONG).show();
            return false;
        }

        if(spinnerPontuacao.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), "Informe o tipo de participação",Toast.LENGTH_LONG).show();
            return false;
        }

        int i = 0;
        List<Questao> qs = new ArrayList<>();
        for(Questao q : DataModel.getInstance().getQuestoesDataModel()){
            boolean adicionado = false;
            if(i == 0){
                if(q.getEnunciado() == null || q.getEnunciado().equals("")){
                    Toast.makeText(getApplicationContext(), "Informe o enunciado de ao menos uma questão",Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                if(q.getEnunciado() == null || q.getEnunciado().equals("")){
                    qs.add(q);
                    adicionado = true;
                }
            }
            i++;

            if(!adicionado){
                boolean b = false;
                for (Alternativa a : q.getAlternativas()) {
                    if(!b)
                        b = a.getCorreta() != 0;
                    else
                        break;
                }

                if(!b){
                    Toast.makeText(getApplicationContext(), "Marque ao menos uma alternativa como correta",Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        DataModel.getInstance().getQuestoesDataModel().removeAll(qs);
        return true;
    }

    @Override
    public void onItemClick(Questao questao, int posicao) {

    }

    public void btnAddQuestaoClicked(View view){
        Questao q = new Questao();
        q.setAlternativas(new ArrayList<>());
        for(int i=0; i<=4; i++){
            q.getAlternativas().add(new Alternativa());
        }
        DataModel.getInstance().getQuestoesDataModel().add(q);

        questionarioAdapter.notifyItemInserted(DataModel.getInstance().getQuestoesDataModel().size()-1);
    }
}