package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.TurmaDisciplinaModel;
import com.pucpr.quester.model.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CadastrarUsuarioActivity extends AppCompatActivity {
    private final String VERIFICAR = "Verificar";

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    String idInstituicao;
    String nomeInstituicao;

    boolean existente;

    EditText editTextCpf;
    EditText editTextNomeUsuario;
    EditText editTextEmailUsuario;
    EditText editTextDtNasc;
    EditText editTextTelUsuario;
    EditText editTextGenero;
    Spinner spinnerPerfil;

    Usuario user;
    Button buttonSalvar;

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
        buttonSalvar = findViewById(R.id.btnSalvarUsuario);

        user = new Usuario();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarUsuarioActivity.this,
                R.array.perfil_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(adapter);

        changeVisibility(View.INVISIBLE);
    }

    public void buttonSalvarClicked(View view) {
        String isVerificar = buttonSalvar.getText().toString();
        if(isVerificar.equals(VERIFICAR)){
            if(isCpf(editTextCpf.getText().toString()))
                verificarExistenciaUsuario();
            else
                Toast.makeText(CadastrarUsuarioActivity.this, "CPF Invalido", Toast.LENGTH_LONG).show();
        }else{
            if(!existente){
                Usuario usuario = new Usuario("", editTextNomeUsuario.getText().toString(), editTextEmailUsuario.getText().toString(),
                        editTextDtNasc.getText().toString(), editTextGenero.getText().toString(), editTextTelUsuario.getText().toString(),
                        editTextCpf.getText().toString(), spinnerPerfil.getSelectedItemPosition());
                criarCredenciais(usuario);
            }else{
                criarUsuario(user);
            }

        }

    }

    public void criarCredenciais(Usuario usuario) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), criarSenha(usuario)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("USUARIO_ID", task.getResult().getUser().getUid());
                    usuario.setIdUsuario(task.getResult().getUser().getUid());
                    criarUsuario(usuario);
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

            List<TurmaDisciplinaModel> ltdm = new ArrayList<>();
            TurmaDisciplinaModel tdm = new TurmaDisciplinaModel("-","-");
            ltdm.add(tdm);

            Professor professor = new Professor(id, usuario.getIdUsuario(), idInstituicao, ltdm);
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

    private void changeVisibility(int visibility) {
        editTextNomeUsuario.setVisibility(visibility);
        editTextEmailUsuario.setVisibility(visibility);
        editTextDtNasc.setVisibility(visibility);
        editTextTelUsuario.setVisibility(visibility);
        editTextGenero.setVisibility(visibility);
        spinnerPerfil.setVisibility(visibility);

        if(visibility != View.INVISIBLE){
            editTextCpf.setEnabled(false);
            buttonSalvar.setText("Salvar");
        }else{
            editTextCpf.setEnabled(true);
            buttonSalvar.setText(VERIFICAR);
        }
    }
    private void verificarExistenciaUsuario() {
        String cpf = editTextCpf.getText().toString();
        if(!cpf.equals("")){
            firestore.collection("usuarios").whereEqualTo("cpf", cpf).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Usuario> lista = task.getResult().toObjects(Usuario.class);
                            if (lista.size() == 0){
                                changeVisibility(View.VISIBLE);
                                existente = false;
                            }else{
                                changeVisibility(View.VISIBLE);
                                existente = true;
                                Query ref = firestore.collection("usuarios").whereEqualTo("cpf", cpf);

                                Task<QuerySnapshot> t = ref.get();

                                t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            pouplarUsuario(task);
                                        }
                                    }
                                });
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Informe o nome da instituição", Toast.LENGTH_LONG).show();
        }
    }
    void pouplarUsuario(Task<QuerySnapshot> t){
        List<Usuario> usuarioList = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));
        Usuario u = usuarioList.get(0);

        editTextNomeUsuario.setText(u.getNome());
        editTextEmailUsuario.setText(u.getEmail());
        editTextDtNasc.setText(u.getDtNascimento());
        editTextTelUsuario.setText(u.getTelefone());
        editTextGenero.setText(u.getGenero());
        spinnerPerfil.setSelection(u.getPerfil());

        editTextNomeUsuario.setEnabled(false);
        editTextDtNasc.setEnabled(false);
        editTextEmailUsuario.setEnabled(false);
        spinnerPerfil.setEnabled(false);
        user = u;
    }

    public boolean isCpf(String cpf){
        if (    cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (InputMismatchException error) {
            return (false);
        }
    }
}