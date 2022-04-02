package com.pucpr.quester.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pucpr.quester.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Instituição");

        firestore = FirebaseFirestore.getInstance();
    }

    public void buttonLoginClicked(View view) {
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        startActivity(i);
    }
}