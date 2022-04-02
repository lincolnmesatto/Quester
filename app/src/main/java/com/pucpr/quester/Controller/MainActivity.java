package com.pucpr.quester.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucpr.quester.R;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    EditText editTextLogin;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void buttonLoginClicked(View view) {
        String email = editTextLogin.getText().toString();
        String senha = editTextPassword.getText().toString();

        loginUser(email, senha);
//        createUser("lincoln.mesatto@gmail.com", "q1w2e3r4");
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
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            Log.e("FIREBASELOGIN", "Login Error" + task.getException().toString());
                        }
                    }
                });
    }

    public void createUser(String login, String senha) {
        auth.createUserWithEmailAndPassword(login, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Cadastrado com sucesso", Toast.LENGTH_LONG);
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Cadastrado sem sucesso", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void buttonCadastroClicked(View view) {
//        String senhaHash = criarHash("lincoln.mesatto", "q1w2e3r4");
//        Usuario usuario = new Usuario("Lincoln Mesatto", "09601876936", "lincoln.mesatto@gmail.com", "lincoln.mesatto" ,senhaHash, "41997475663", "Masculino", 3);
//        UsuarioDataModel.getInstance().addUsuario(usuario);
    }

}