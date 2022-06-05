package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    String idInstituicao;
    String nomeInstituicao;

    EditText editTextCpf;
    EditText editTextNomeUsuario;
    EditText editTextEmailUsuario;
    EditText editTextDtNasc;
    EditText editTextTelUsuario;
    EditText editTextGenero;
    Spinner spinnerPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
        }

        editTextCpf = findViewById(R.id.editTextCpf);
        spinnerPerfil = findViewById(R.id.spinnerPerfil);
        editTextDtNasc = findViewById(R.id.editTextDtNasc);
        editTextGenero = findViewById(R.id.editTextGenero);
        editTextTelUsuario = findViewById(R.id.editTextTelUsuario);
        editTextNomeUsuario = findViewById(R.id.editTextNomeUsuario);
        editTextEmailUsuario = findViewById(R.id.editTextEmailUsuario);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarUsuarioActivity.this,
                R.array.perfil_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(adapter);
    }

    public void buttonSalvarClicked(View view) {
        Usuario usuario = new Usuario("", editTextNomeUsuario.getText().toString(), editTextEmailUsuario.getText().toString(),
                editTextDtNasc.getText().toString(), editTextGenero.getText().toString(), editTextTelUsuario.getText().toString(),
                editTextCpf.getText().toString(), spinnerPerfil.getSelectedItemPosition());

        criarCredenciais(usuario);
    }

    public void criarCredenciais(Usuario usuario) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), criarSenha(usuario)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("USUARIO_ID", task.getResult().getUser().getUid());
                    usuario.setIdUsuario(task.getResult().getUser().getUid());
                    criarUsuario(usuario);
                } else {
                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro ao criar credenciais do usuário", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private String criarSenha(Usuario usuario) {
        String senha = "";

        char[] sequence = usuario.getNome().toLowerCase(Locale.ROOT).toCharArray();
        senha = sequence[0]+""+sequence[1]+""+sequence[2]+"..";
        sequence = usuario.getCpf().toCharArray();
        senha += sequence[0]+""+sequence[1]+""+sequence[2];

        return senha;
    }

    void criarUsuario(Usuario usuario) {
        firestore.collection("usuarios").document(usuario.getIdUsuario()).set(usuario);

        if(usuario.getPerfil() == 1){
            DocumentReference ref = firestore.collection("alunos").document();
            String id = ref.getId();

            List<String> turmas = new ArrayList<>();
            turmas.add("-");

            Aluno aluno = new Aluno(id, usuario.getIdUsuario(), idInstituicao, 0f, 1, turmas);
            firestore.collection("alunos").document(aluno.getId()).set(aluno);
        }else{
            DocumentReference ref = firestore.collection("professores").document();
            String id = ref.getId();

            Professor professor = new Professor(id, usuario.getIdUsuario(), idInstituicao);
            firestore.collection("professores").document(professor.getId()).set(professor);
        }

        firestore.collection("usuarios").
                document(usuario.getIdUsuario()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Usuario u = value.toObject(Usuario.class);
                            Toast.makeText(getApplicationContext(), "Usuário(a) "+ (u != null ? u.getNome() : null) +
                                    " adicionado(a) com sucesso(a)", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Intent i = new Intent(CadastrarUsuarioActivity.this, InsituicaoDerivadoActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CadastrarUsuarioActivity.this, InsituicaoDerivadoActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}