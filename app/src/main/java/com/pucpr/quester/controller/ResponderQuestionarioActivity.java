package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
import com.pucpr.quester.model.AlternativaResposta;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.DataModelResposta;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questao;
import com.pucpr.quester.model.QuestaoResposta;
import com.pucpr.quester.model.Questionario;
import com.pucpr.quester.model.Resposta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResponderQuestionarioActivity extends AppCompatActivity implements ResponderQuestionarioAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    TextView tvResponderDisciplina;
    TextView tvResponderXp;
    TextView tvResponderTitulo;

    RecyclerView recyclerViewQuestoesResposta;

    String idAluno;
    String idInstituicao;
    String idTurma;
    String idQuestionario;

    ArrayList<String> turmas;

    ResponderQuestionarioAdapter questionarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_questionario);

        setTitle("Responder Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DataModelResposta.getInstance().setContext(ResponderQuestionarioActivity.this);

        tvResponderDisciplina = findViewById(R.id.tvResponderDisciplina);
        tvResponderXp = findViewById(R.id.tvResponderXp);
        tvResponderTitulo = findViewById(R.id.tvResponderTitulo);
        recyclerViewQuestoesResposta = findViewById(R.id.recyclerViewQuestoesResposta);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
            idQuestionario = extras.getString("idQuestionario");

            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        popularListaQuestionario(idQuestionario);
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

        DataModelResposta.getInstance().setQuestoesDataModel(new ArrayList<>());

        DataModelResposta.getInstance().getQuestoesDataModel().addAll(q.getQuestoes());

        DataModelResposta.getInstance().setQuestoesRespostaDataModel(new ArrayList<>());

        for (Questao questao : DataModelResposta.getInstance().getQuestoesDataModel()) {
            List<Alternativa> temp = new ArrayList<>();
            List<AlternativaResposta> listaAlternativa = new ArrayList<>();

            for (Alternativa a : questao.getAlternativas()) {
                if(a.getAlternativa() == null){
                    temp.add(a);
                }else{
                    listaAlternativa.add(new AlternativaResposta());
                }
            }

            questao.getAlternativas().removeAll(temp);

            DataModelResposta.getInstance().getQuestoesRespostaDataModel().add(new QuestaoResposta(listaAlternativa));
        }

        tvResponderTitulo.setText(q.getTitulo());
        tvResponderXp.setText(String.valueOf(q.getXp()));

        popularListaDisciplina(q.getIdDisciplina());

        questionarioAdapter = new ResponderQuestionarioAdapter(this, DataModelResposta.getInstance().getQuestoesDataModel());

        recyclerViewQuestoesResposta.setHasFixedSize(true);
        recyclerViewQuestoesResposta.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestoesResposta.setAdapter(questionarioAdapter);
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

        tvResponderDisciplina.setText(d.getNome());
    }

    @Override
    public void onItemClick(Questao questao, int posicao) {

    }

    public void responderQuestionario(View view){
        if(validarSalvar()){
            DocumentReference ref = firestore.collection("respostas").document();
            String idResposta = ref.getId();

            Resposta resposta = new Resposta(idResposta, idQuestionario, idAluno, DataModelResposta.getInstance().getQuestoesRespostaDataModel());

            firestore.collection("respostas").document(idResposta).set(resposta);

            Intent intent = new Intent(ResponderQuestionarioActivity.this, QuestionarioAlunoActivity.class);
            intent.putStringArrayListExtra("turmas", turmas);
            intent.putExtra("idInstituicao", idInstituicao);
            intent.putExtra("idAluno", idAluno);
            intent.putExtra("idTurma", idTurma);
            startActivity(intent);
        }
    }

    public boolean validarSalvar(){
        for (QuestaoResposta qr: DataModelResposta.getInstance().getQuestoesRespostaDataModel()) {
            boolean respondido = false;
            for (AlternativaResposta ar:qr.getAlternativas()) {
                if(ar.getCorreta() == 1){
                    respondido = true;
                    break;
                }
            }

            if(!respondido){
                Toast.makeText(getApplicationContext(),"Marque ao menos uma alternativa para cada questão!", Toast.LENGTH_LONG).show();
                return respondido;
            }
        }

        return true;
    }
}