package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Instituicao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class CadastrarDisciplinaActivity extends AppCompatActivity {
    private final String VERIFICAR = "Verificar";

    FirebaseFirestore firestore;

    String id;
    EditText editTextNomeDisciplina;
    Button btnSalvarDisciplina;
    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_disciplina);

        setTitle("Cadastrar Disciplina");

        editTextNomeDisciplina = findViewById(R.id.editTextNomeDisciplina);
        btnSalvarDisciplina = findViewById(R.id.btnSalvarDisciplina);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);

        mAwesomeValidation.addValidation(this, R.id.editTextNomeDisciplina, RegexTemplate.NOT_EMPTY, R.string.err_vazio);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");

            DocumentReference docRef = firestore.collection("disciplinas").document(id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Disciplina d = documentSnapshot.toObject(Disciplina.class);

                    editTextNomeDisciplina.setText(d.getNome());
                }
            });

        }else{
            id = "none";
        }

        changeVisibility(id.equals("none") ? View.INVISIBLE : View.VISIBLE);
    }

    private void changeVisibility(int visibility) {
        if(visibility != View.INVISIBLE){
            btnSalvarDisciplina.setText("Salvar");
        }else{
            btnSalvarDisciplina.setText(VERIFICAR);
        }
    }

    public void buttonSalvarClicked(View view) {
        String isVerificar = btnSalvarDisciplina.getText().toString();
        if(isVerificar.equals(VERIFICAR)){
            verificarExistencia();
        }else{
            if (id == "none") {
                DocumentReference ref = firestore.collection("disciplinas").document();
                id = ref.getId();
            }
            Disciplina disciplina = new Disciplina(id, editTextNomeDisciplina.getText().toString(), 1);

            if(mAwesomeValidation.validate()){
                criarDisciplina(disciplina);
            }else {
                Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verificarExistencia(){
        String nome = editTextNomeDisciplina.getText().toString();

        if(!nome.equals("")){
            firestore.collection("disciplinas").whereEqualTo("nome", nome).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Disciplina> lista = task.getResult().toObjects(Disciplina.class);
                            if (lista.size() == 0)
                                changeVisibility(View.VISIBLE);
                            else {
                                Toast.makeText(getApplicationContext(), "Disciplina " + nome + " já cadastrada.", Toast.LENGTH_LONG).show();
                                changeVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Informe o nome da disciplina", Toast.LENGTH_LONG).show();
        }
    }

    void criarDisciplina(Disciplina disciplina) {
        firestore.collection("disciplinas").
                document(disciplina.getId()).set(disciplina);

        firestore.collection("disciplinas").
                document(disciplina.getId()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Disciplina d = value.toObject(Disciplina.class);
                            Toast.makeText(getApplicationContext(), "Disciplina "+ (d != null ? d.getNome() : null) +
                                    " adicionada com sucesso ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Intent i = new Intent(CadastrarDisciplinaActivity.this, DisciplinaActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CadastrarDisciplinaActivity.this, DisciplinaActivity.class);
        startActivity(i);
    }
}