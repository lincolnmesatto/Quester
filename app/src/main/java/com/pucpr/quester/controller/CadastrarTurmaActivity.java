package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.pucpr.quester.model.Turma;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CadastrarTurmaActivity extends AppCompatActivity {
    FirebaseFirestore firestore;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    EditText editTextCadastroNomeTurma;
    EditText editTextCadastroDtInicioTurma;
    EditText editTextCadastroDtFimTurma;
    EditText editTextCadastroSerieTurma;
    Button btnCadastraTurma;
    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_turma);

        setTitle("Cadastrar Turma");

        editTextCadastroNomeTurma = findViewById(R.id.editTextCadastroNomeTurma);
        editTextCadastroDtInicioTurma = findViewById(R.id.editTextCadastroDtInicioTurma);
        editTextCadastroDtFimTurma = findViewById(R.id.editTextCadastroDtFimTurma);
        editTextCadastroSerieTurma = findViewById(R.id.editTextCadastroSerieTurma);
        btnCadastraTurma = findViewById(R.id.btnCadastrarTurma);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);

        mAwesomeValidation.addValidation(this, R.id.editTextCadastroNomeTurma, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextCadastroDtInicioTurma, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextCadastroDtFimTurma, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextCadastroSerieTurma, RegexTemplate.NOT_EMPTY, R.string.err_vazio);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");

            if(!id.equals("none")) {
                DocumentReference docRef = firestore.collection("turmas").document(id);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Turma t = documentSnapshot.toObject(Turma.class);

                        editTextCadastroNomeTurma.setText(t.getNomeTurma());
                        editTextCadastroDtInicioTurma.setText(t.getDataInicioVigencia());
                        editTextCadastroDtFimTurma.setText(t.getDataFimVigencia());
                        editTextCadastroSerieTurma.setText(Integer.toString(t.getSerie()));
                    }
                });
            }
        }else{
            id = "none";
        }
    }
    public void buttonSalvarClicked(View view) {
        try {
            if (id.equals("none")) {
                DocumentReference ref = firestore.collection("turmas").document();
                id = ref.getId();
            }

            if(mAwesomeValidation.validate()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataDe = dateFormat.parse(editTextCadastroDtInicioTurma.getText().toString());
                Date dataPara = dateFormat.parse(editTextCadastroDtFimTurma.getText().toString());
                if(dataPara.before(dataDe)){
                    Toast.makeText(getApplicationContext(), "A Data Fim da Vigência deve ser maior que a Data Início",Toast.LENGTH_LONG).show();
                }else {
                    Turma turma = new Turma(id, editTextCadastroNomeTurma.getText().toString(), editTextCadastroDtInicioTurma.getText().toString(),
                            editTextCadastroDtFimTurma.getText().toString(), Integer.valueOf(editTextCadastroSerieTurma.getText().toString()), idInstituicao);

                    criarTurma(turma);
                }
            }else {
                Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void criarTurma(Turma turma) {
        firestore.collection("turmas").
                document(turma.getId()).set(turma);

        firestore.collection("turmas").
                document(turma.getId()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Turma turma = value.toObject(Turma.class);
                            Toast.makeText(getApplicationContext(), "Turmas "+ (turma != null ? turma.getNomeTurma() : null) +
                                    " adicionada com sucesso ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Intent i = new Intent(CadastrarTurmaActivity.this, TurmaActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CadastrarTurmaActivity.this, TurmaActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}
