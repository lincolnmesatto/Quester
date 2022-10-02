package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;

public class AlunoInstituicaoDerivadoActivity extends AppCompatActivity {

    ArrayList<String> turmas;

    String idAluno;
    String idInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_instituicao_derivado);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            turmas = getIntent().getStringArrayListExtra("turmas");
        }
    }

    public void buttonTurmaClicked(View view){
        Intent intent = new Intent(AlunoInstituicaoDerivadoActivity.this, AlunoTurmaActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }

    public void buttonPerfilClicked(View view){
        Intent intent = new Intent(AlunoInstituicaoDerivadoActivity.this, AlunoDerivadoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }

    public void buttonRecompensaClicked(View view){
        Intent intent = new Intent(AlunoInstituicaoDerivadoActivity.this, RecompensaAlunoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }
}