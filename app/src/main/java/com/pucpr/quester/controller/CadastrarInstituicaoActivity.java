package com.pucpr.quester.controller;

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
import com.pucpr.quester.model.Instituicao;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CadastrarInstituicaoActivity extends AppCompatActivity {
    private final String VERIFICAR = "Verificar";

    FirebaseFirestore firestore;

    EditText editTextNome;
    EditText editTextTelefone;
    EditText editTextUF;
    EditText editTextCidade;
    EditText editTextBairro;
    EditText editTextLogradouro;
    EditText editTextComplemento;
    EditText editTextNumero;

    Button buttonSalvar;

    String id;

    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_instituicao);

        setTitle("Cadastrar Instituição");

        editTextNome = findViewById(R.id.editTextNome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextUF = findViewById(R.id.editTextUF);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextLogradouro = findViewById(R.id.editTextLogradouro);
        editTextComplemento = findViewById(R.id.editTextComplemento);
        editTextNumero = findViewById(R.id.editTextNumero);

        buttonSalvar = findViewById(R.id.buttonSalvar);

        //Validacao Style
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);

        mAwesomeValidation.addValidation(this, R.id.editTextNome, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.editTextTelefone, RegexTemplate.TELEPHONE, R.string.err_tel);
        mAwesomeValidation.addValidation(this, R.id.editTextUF, "[A-Z]{2}", R.string.err_uf);
        mAwesomeValidation.addValidation(this, R.id.editTextUF,RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextCidade, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextBairro,RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextLogradouro,RegexTemplate.NOT_EMPTY, R.string.err_vazio);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");

            DocumentReference docRef = firestore.collection("instituicoes").document(id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Instituicao i = documentSnapshot.toObject(Instituicao.class);

                    editTextNome.setText(i.getNome());
                    editTextTelefone.setText(i.getTelefone());
                    editTextUF.setText(i.getEstado());
                    editTextCidade.setText(i.getCidade());
                    editTextBairro.setText(i.getBairro());
                    editTextLogradouro.setText(i.getLogradouro());
                    editTextComplemento.setText(i.getComplemento());
                    editTextNumero.setText(i.getNumero());
                }
            });

        }else{
            id = "none";
        }

        changeVisibility(id.equals("none") ? View.INVISIBLE : View.VISIBLE);
    }

    private void changeVisibility(int visibility) {
        editTextTelefone.setVisibility(visibility);
        editTextUF.setVisibility(visibility);
        editTextCidade.setVisibility(visibility);
        editTextBairro.setVisibility(visibility);
        editTextLogradouro.setVisibility(visibility);
        editTextComplemento.setVisibility(visibility);
        editTextNumero.setVisibility(visibility);

        if(visibility != View.INVISIBLE){
            editTextNome.setEnabled(false);
            buttonSalvar.setText("Salvar");
        }else{
            editTextNome.setEnabled(true);
            buttonSalvar.setText(VERIFICAR);
        }
    }

    public void buttonSalvarClicked(View view) {
        String isVerificar = buttonSalvar.getText().toString();
        if(isVerificar.equals(VERIFICAR)){
            verificarExistenciaInstituicao();
        }else {
            if (id == "none") {
                DocumentReference ref = firestore.collection("instituicoes").document();
                id = ref.getId();
            }
            Instituicao instituicao = new Instituicao(id, editTextNome.getText().toString(), editTextUF.getText().toString(),
                    editTextCidade.getText().toString(), editTextBairro.getText().toString(),
                    editTextLogradouro.getText().toString(), editTextNumero.getText().toString(),
                    editTextComplemento.getText().toString(), editTextTelefone.getText().toString());

            if(mAwesomeValidation.validate()){
                criarInstituicao(instituicao);
            }else {
                Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void verificarExistenciaInstituicao() {
        String nome = editTextNome.getText().toString();
        if(!nome.equals("")){
            firestore.collection("instituicoes").whereEqualTo("nome", nome).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Instituicao> lista = task.getResult().toObjects(Instituicao.class);
                            if (lista.size() == 0)
                                changeVisibility(View.VISIBLE);
                            else
                                Toast.makeText(getApplicationContext(), "Instituição " + nome + " já cadastrada.", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Informe o nome da instituição", Toast.LENGTH_LONG).show();
        }
    }

    void criarInstituicao(Instituicao instituicao) {
        firestore.collection("instituicoes").
                document(instituicao.getId()).set(instituicao);

        firestore.collection("instituicoes").
                document(instituicao.getId()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Instituicao instituicao = value.toObject(Instituicao.class);
                            Toast.makeText(getApplicationContext(), "Instituição "+ (instituicao != null ? instituicao.getNome() : null) +
                                            " adicionada com sucesso ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Intent i = new Intent(CadastrarInstituicaoActivity.this, HomeActivity.class);
        startActivity(i);
    }
}