package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlunoDerivadoActivity extends AppCompatActivity {

    ArrayList<String> turmas;

    String idAluno;
    String idInstituicao;

    private TextView editProfileName, editProfileBirthDate, editProfilePhone;
    private TextView profileName;
    private static final String USERS = "users";

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_derivado);

        editProfileName = findViewById(R.id.editProfileName);
        editProfileBirthDate = findViewById(R.id.editProfileBirthDate);
        editProfilePhone = findViewById(R.id.editProfilePhone);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        usuario = new Usuario();
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            turmas = getIntent().getStringArrayListExtra("turmas");
        }
        buscarUsuario();

    }
    public void buscarUsuario(){
        Query ref = firestore.collection("usuarios").whereEqualTo("idUsuario", firebaseUser.getUid());

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    popularUsuario(task);
                }
            }
        });
    }

    public void popularUsuario(Task<QuerySnapshot> task){
        List<Usuario> lista = Objects.requireNonNull(task.getResult().toObjects(Usuario.class));

        usuario = lista.get(0);
        editProfileName.setText(usuario.getNome());
        editProfileBirthDate.setText(usuario.getDtNascimento());
        editProfilePhone.setText(usuario.getTelefone());
    }

    public void salvarPerfil(View view) {
        usuario.setNome(editProfileName.getText().toString());
        usuario.setDtNascimento(editProfileBirthDate.getText().toString());
        usuario.setTelefone(editProfilePhone.getText().toString());

        firestore.collection("usuarios").document(usuario.getIdUsuario()).set(usuario);

        Intent intent = new Intent(AlunoDerivadoActivity.this, AlunoInstituicaoDerivadoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }

}