package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.pucpr.quester.controller.adapter.ResponderQuestionarioAdapter;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.AlternativaResposta;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Classe;
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
    Button btnSalvarRespsta;

    RecyclerView recyclerViewQuestoesResposta;

    String idAluno;
    String idInstituicao;
    String idTurma;
    String idQuestionario;

    ArrayList<String> turmas;
    Aluno aluno;

    Questionario questionario;
    Resposta resposta;
    Classe classe;

    ResponderQuestionarioAdapter questionarioAdapter;

    int tipoPontuacao;
    boolean isRespondido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_questionario);

        setTitle("Responder Questionários");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DataModelResposta.getInstance().setContext(ResponderQuestionarioActivity.this);
        DataModelResposta.getInstance().setQuestoesRespostaDataModel(new ArrayList<>());

        tvResponderDisciplina = findViewById(R.id.tvResponderDisciplina);
        tvResponderXp = findViewById(R.id.tvResponderXp);
        tvResponderTitulo = findViewById(R.id.tvResponderTitulo);
        recyclerViewQuestoesResposta = findViewById(R.id.recyclerViewQuestoesResposta);
        btnSalvarRespsta = findViewById(R.id.btnSalvarRespsta);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
            idQuestionario = extras.getString("idQuestionario");

            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        questionario = new Questionario();
        classe = new Classe();

        verificarExistenciaResposta();
        popularAluno(idAluno);

        popularListaQuestionario(idQuestionario);
    }

    private void popularAluno(String idAluno) {
        Query ref = firestore.collection("alunos").whereEqualTo("id", idAluno);
        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarAluno(task);
                }
            }
        });
    }

    private void buscarAluno(Task<QuerySnapshot> task) {
        List<Aluno> alunos = Objects.requireNonNull(task.getResult().toObjects(Aluno.class));
        aluno = alunos.get(0);

        obterClasse();
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

        questionario = q;

        tipoPontuacao = q.getTipoPontuacao();

        DataModelResposta.getInstance().setQuestoesDataModel(new ArrayList<>());
        DataModelResposta.getInstance().getQuestoesDataModel().addAll(q.getQuestoes());

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

            if(!isRespondido)
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

            Resposta r = new Resposta(idResposta, idQuestionario, idAluno, DataModelResposta.getInstance().getQuestoesRespostaDataModel());

            firestore.collection("respostas").document(idResposta).set(r);

            resposta = r;

            calcularExperiencia(tvResponderXp.getText().toString(), tipoPontuacao);

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

    private void calcularExperiencia(String xp, int tipoPontuacao) {
        //1 - participacao 2 - acerto
        double xpCalculado = 0;
        if(tipoPontuacao <= 1){
            if(classe.getId().equals("none"))
                xpCalculado = aluno.getXp()+ Double.valueOf(xp);
            else{
                if(classe.getIdDisciplina().equals(questionario.getIdDisciplina()))
                    xpCalculado = aluno.getXp()+(Double.valueOf(xp) + ((Double.valueOf(classe.getBonus())/100) * Double.valueOf(xp)));
                else
                    xpCalculado = aluno.getXp()+ Double.valueOf(xp);
            }

            aluno.setXp(xpCalculado);
            aluno.setLevel(calcularLevel());
        }else{
            double xpCorrigido = calcularExperienciaAcerto();
            if(classe.getId().equals("none"))
                xpCalculado = aluno.getXp() + Double.valueOf(xpCorrigido);
            else{
                if(classe.getIdDisciplina().equals(questionario.getIdDisciplina()))
                    xpCalculado = aluno.getXp() + (Double.valueOf(xpCorrigido) + ((Double.valueOf(classe.getBonus())/100) * Double.valueOf(xpCorrigido)));
                else
                    xpCalculado = aluno.getXp() + Double.valueOf(xpCorrigido);
            }

            aluno.setXp(xpCalculado);
            aluno.setLevel(calcularLevel());
        }

        firestore.collection("alunos").document(idAluno).set(aluno);
    }

    public double calcularExperienciaAcerto(){
        int errada = 0;
        int posicao = 0;
        for (QuestaoResposta qr : resposta.getQuestoesResposta()) {
            Questao qq = questionario.getQuestoes().get(posicao);

            int posicaoAlternativa = 0;
            for (AlternativaResposta ar : qr.getAlternativas()) {
                Alternativa aq = qq.getAlternativas().get(posicaoAlternativa);
                if(ar.getCorreta() != aq.getCorreta()){
                    errada++;
                    break;
                }

                posicaoAlternativa++;
            }

            posicao++;
        }

       return errada == 0 ? questionario.getXp() : (errada == questionario.getQuestoes().size() ? 0 :
               questionario.getXp() - ((Double.valueOf(questionario.getXp()) * errada) / questionario.getQuestoes().size()));
    }

    private int calcularLevel() {
        int level = 1;
        int p = (level * 1000)+(level *100);

        while(aluno.getXp() > p){
            p = ((level+1) * 1000)+((level+1) *100);
            if(aluno.getXp() > p)
                level++;
        }

        return level;
    }

    public void verificarExistenciaResposta(){
        Query ref = firestore.collection("respostas").whereEqualTo("idQuestionario", idQuestionario).whereEqualTo("idAluno", idAluno);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarResposta(task);
                }
            }
        });
    }

    private void buscarResposta(Task<QuerySnapshot> task) {
        List<Resposta> respostas = Objects.requireNonNull(task.getResult().toObjects(Resposta.class));

        if(respostas.size() > 0){
            resposta = respostas.get(0);
            isRespondido = true;
            DataModelResposta.getInstance().setRespondido(isRespondido);
            btnSalvarRespsta.setEnabled(false);

            for (Resposta r : respostas) {
                DataModelResposta.getInstance().getQuestoesRespostaDataModel().addAll(r.getQuestoesResposta());
            }
        }else{
            DataModelResposta.getInstance().setRespondido(false);
        }
    }

    private void obterClasse() {
        if(!aluno.getIdClasse().equals("-")){
            Query ref = firestore.collection("classes").whereEqualTo("id", aluno.getIdClasse());

            Task<QuerySnapshot> t = ref.get();

            t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        buscarClasse(task);
                    }
                }
            });
        }else{
            classe.setId("none");
        }
    }

    private void buscarClasse(Task<QuerySnapshot> task) {
        List<Classe> classes = Objects.requireNonNull(task.getResult().toObjects(Classe.class));
        classe = classes.get(0);
    }
}