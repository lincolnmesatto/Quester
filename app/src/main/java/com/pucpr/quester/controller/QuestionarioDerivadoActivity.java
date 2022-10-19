package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.TurmaAluno2Adapter;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.AlternativaResposta;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Questao;
import com.pucpr.quester.model.QuestaoResposta;
import com.pucpr.quester.model.Questionario;
import com.pucpr.quester.model.Resposta;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionarioDerivadoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    PieChart pieChart;

    String idProfessor;
    String idInstituicao;
    String idTurma;
    String idQuestionario;

    List<Aluno> alunos;
    List<Resposta> respostas;
    Questionario questionario;

    int posicao;
    List<Integer> porcentagemAcertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionario_derivado);
        pieChart = findViewById(R.id.graficoQuestionario);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
            idQuestionario = extras.getString("idQuestionario");
        }

        alunos = new ArrayList<>();
        respostas = new ArrayList<>();
        questionario = new Questionario();
        porcentagemAcertos = new ArrayList<>();
        for(int i=0;i<3;i++)
            porcentagemAcertos.add(0);

        posicao = 0;

        buscarAlunos();
        buscarRespostas();
        buscarQuestionario();
    }

    public void buscarAlunos(){
        Query ref = firestore.collection("alunos").whereEqualTo("idInsituicao", idInstituicao).whereArrayContains("turmas", idTurma);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularAlunos(task);
                }
            }
        });
    }

    void popularAlunos(Task<QuerySnapshot> t){
        alunos = Objects.requireNonNull(t.getResult().toObjects(Aluno.class));
    }

    public void buscarRespostas(){
        Query ref = firestore.collection("respostas").whereEqualTo("idQuestionario", idQuestionario);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularResposta(task);
                }
            }
        });
    }

    void popularResposta(Task<QuerySnapshot> t){
        respostas = Objects.requireNonNull(t.getResult().toObjects(Resposta.class));

        montarGraficoParticipação();
    }

    public void buscarQuestionario(){
        Query ref = firestore.collection("questionarios").whereEqualTo("id", idQuestionario);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularQuestionario(task);
                }
            }
        });
    }

    void popularQuestionario(Task<QuerySnapshot> t){
        List<Questionario> questionarios = Objects.requireNonNull(t.getResult().toObjects(Questionario.class));
        questionario = questionarios.get(0);
    }

    public void btnNextClicked(View view){
        posicao = posicao == 0 ? 1 : 0;

        switch (posicao){
            case 0:
                montarGraficoParticipação();
                break;
            case 1:
                montarGraficoAcerto();
                break;
            default:
                break;
        }
    }

    public void montarGraficoParticipação(){
        pieChart.setUsePercentValues(true);

        Description desc = new Description();
        desc.setText("Participação nos Questionários");
        desc.setTextSize(20f);
        pieChart.setDescription(desc);

        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(25f);

        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(respostas.size(), "Respostas"));
        value.add(new PieEntry((alunos.size() - respostas.size()), "Não responderam"));

        PieDataSet pieDataSet = new PieDataSet(value, "Participação");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        pieChart.animateXY(1400, 1400);
    }

    public void montarGraficoAcerto(){
        pieChart.setUsePercentValues(true);

        calcularAcertos();

        Description desc = new Description();
        desc.setText("% de Acerto");
        desc.setTextSize(20f);
        pieChart.setDescription(desc);

        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(25f);

        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(porcentagemAcertos.get(0), "0% ~ 40%"));
        value.add(new PieEntry(porcentagemAcertos.get(1), "40% ~ 70%"));
        value.add(new PieEntry(porcentagemAcertos.get(2), "70% ~ 100%"));

        PieDataSet pieDataSet = new PieDataSet(value, "Acertos");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        pieChart.animateXY(1400, 1400);
    }

    public void calcularAcertos(){
        for (Resposta resposta:respostas) {
            int errada = 0;
            int posicao = 0;
            int total = resposta.getQuestoesResposta().size();

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

            int acerto = total - errada;
            float percent = (acerto * 100)/total;

            if(percent < 40){
                int i = porcentagemAcertos.get(0)+1;
                porcentagemAcertos.set(0, i);
            }else if(percent >= 40 && percent <70){
                int i = porcentagemAcertos.get(0)+1;
                porcentagemAcertos.set(2, i);
            }else {
                int i = porcentagemAcertos.get(0)+1;
                porcentagemAcertos.set(3, i);
            }

        }
    }
}