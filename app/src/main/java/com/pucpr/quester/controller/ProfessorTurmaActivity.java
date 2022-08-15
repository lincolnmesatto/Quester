package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Professor;

public class ProfessorTurmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_turma);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        Professor p = (Professor)bundle.getSerializable("value");
        p.getTdm();
    }
}