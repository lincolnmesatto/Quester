package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Classe;

public class MenuAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        setTitle("Menu Admin");
    }

    public void buttonInstuicoesClicked(View view){
        Intent i = new Intent(MenuAdminActivity.this, HomeActivity.class);
        startActivity(i);
    }

    public void buttonClassesClicked(View view){
        Intent i = new Intent(MenuAdminActivity.this, ClasseActivity.class);
        startActivity(i);
    }

    public void buttonDisciplinasClicked(View view){
        Intent i = new Intent(MenuAdminActivity.this, DisciplinaActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MenuAdminActivity.this, MainActivity.class);
        startActivity(i);
    }
}