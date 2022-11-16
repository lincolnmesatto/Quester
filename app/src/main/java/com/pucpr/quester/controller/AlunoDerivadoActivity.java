package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Classe;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlunoDerivadoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    EditText editProfileName;
    EditText editProfileBirthDate;
    EditText editProfilePhone;
    TextView textViewDescClasse;
    Spinner classeSpinner;

    List<Disciplina> disciplinas;
    List<Classe> classes;
    ArrayList<String> turmas;

    String idAluno;
    String idInstituicao;

    Usuario usuario;
    Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_derivado);

        editProfileName = findViewById(R.id.editProfileName);
        editProfileBirthDate = findViewById(R.id.editProfileBirthDate);
        editProfilePhone = findViewById(R.id.editProfilePhone);
        classeSpinner = findViewById(R.id.classeSpinner);
        textViewDescClasse = findViewById(R.id.textViewDescClasse);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        usuario = new Usuario();
        classes = new ArrayList<>();
        disciplinas = new ArrayList<>();

        setTitle("Perfil Aluno");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        popularListaDisciplina();
        popularAluno();
        popularClasse();
        buscarUsuario();

        classeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position != 0){
                    int i = position-1;
                    Classe c = classes.get(i);
                    String nome = "";
                    for (Disciplina d : disciplinas){
                        if(d.getId().equals(c.getIdDisciplina())) {
                            nome = d.getNome();
                            break;
                        }
                    }
                    textViewDescClasse.setText("Recebe "+c.getBonus()+"% a mais de experiência em questionários da disciplina "+nome);
                }else{
                    textViewDescClasse.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
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

        if(classeSpinner.getSelectedItemPosition() != 0){
            int i = classeSpinner.getSelectedItemPosition();
            aluno.setIdClasse(classes.get(classeSpinner.getSelectedItemPosition()-1).getId());
            firestore.collection("alunos").document(idAluno).set(aluno);
        }

        firestore.collection("usuarios").document(usuario.getIdUsuario()).set(usuario);

        Intent intent = new Intent(AlunoDerivadoActivity.this, AlunoInstituicaoDerivadoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }

    private void popularListaDisciplina() {
        Query ref = firestore.collection("disciplinas");

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarDisciplina(task);
                }
            }
        });
    }

    private void buscarDisciplina(Task<QuerySnapshot> task) {
        disciplinas = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
    }

    private void popularAluno() {
        Query ref = firestore.collection("alunos").whereEqualTo("id", idAluno);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarAluno(task);
                }
            }
        });
    }

    private void buscarAluno(Task<QuerySnapshot> task) {
        List<Aluno> alunos = Objects.requireNonNull(task.getResult().toObjects(Aluno.class));
        aluno = alunos.get(0);
    }

    private void popularClasse() {
        Query ref = firestore.collection("classes");

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarClasse(task);
                }
            }
        });
    }

    private void buscarClasse(Task<QuerySnapshot> task) {
        classes = Objects.requireNonNull(task.getResult().toObjects(Classe.class));
        ArrayList<String> nomesClasses = new ArrayList<>();
        nomesClasses.add("Classe");

//        int i = 0;
        for(Classe c : classes){
//            if(!aluno.getIdClasse().equals("-")){
//                if(!c.getId().equals(aluno.getIdClasse()))
//                    i++;
//            }

            nomesClasses.add(c.getNome());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesClasses);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classeSpinner.setAdapter(spinnerArrayAdapter);

        for(int i = 0; i<=classes.size();i++){
            if(!aluno.getIdClasse().equals("-")) {
                if (classes.get(i).getId().equals(aluno.getIdClasse())) {
                    classeSpinner.setSelection(i + 1);
                    break;
                }

                classeSpinner.setEnabled(false);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlunoDerivadoActivity.this, AlunoInstituicaoDerivadoActivity.class);
        intent.putStringArrayListExtra("turmas", turmas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idAluno", idAluno);
        startActivity(intent);
    }
}