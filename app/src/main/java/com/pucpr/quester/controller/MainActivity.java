package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucpr.quester.R;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    EditText editTextLogin;
    EditText editTextPassword;

    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);

        mAwesomeValidation.addValidation(this, R.id.editTextLogin, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextPassword,RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextLogin, "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}", R.string.err_email);
    }

    public void buttonLoginClicked(View view) {
        String email = editTextLogin.getText().toString();
        String senha = editTextPassword.getText().toString();

        if(mAwesomeValidation.validate()){
            loginUser(email, senha);
        }else {
            Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
        }
        //loginUser("lincoln.mesatto@gmail.com", "q1w2e3r4");
    }

    public void loginUser(String login, String senha) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(login, senha).
                addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "E-mail e/ou senha incorretos.", Toast.LENGTH_LONG).show();
                            Log.e("FIREBASELOGIN", "Login Error" + task.getException().toString());
                        }
                    }
                });
    }
}