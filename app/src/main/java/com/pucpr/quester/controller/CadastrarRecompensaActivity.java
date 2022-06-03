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
import com.pucpr.quester.model.Recompensa;
import com.pucpr.quester.model.Turma;

import java.util.List;

public class CadastrarRecompensaActivity extends AppCompatActivity {
    private final String VERIFICAR = "Verficar";
    FirebaseFirestore firestore;

    String id;
    String idInstituicao;
    String nomeInstituicao;

    EditText editTextDescricaoDisciplina;
    EditText editTextLvlAdquire;
    Button btnSalvarRecompensa;

    AwesomeValidation mAwesomeValidation;

    String oldLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_recompensa);

        setTitle("Cadastrar Recompensa");

        editTextDescricaoDisciplina = findViewById(R.id.editTextDescricaoDisciplina);
        editTextLvlAdquire = findViewById(R.id.editTextLvlAdquire);
        btnSalvarRecompensa = findViewById(R.id.btnSalvarRecompensa);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(Color.RED);

        mAwesomeValidation.addValidation(this, R.id.editTextDescricaoDisciplina, RegexTemplate.NOT_EMPTY, R.string.err_vazio);
        mAwesomeValidation.addValidation(this, R.id.editTextLvlAdquire, RegexTemplate.NOT_EMPTY, R.string.err_vazio);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");

            if(!id.equals("none")) {
                DocumentReference docRef = firestore.collection("recompensas").document(id);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Recompensa r = documentSnapshot.toObject(Recompensa.class);

                        editTextDescricaoDisciplina.setText(r.getDescricao());
                        editTextLvlAdquire.setText(Integer.toString(r.getLevelAdquire()));
                    }
                });
            }
        }else{
            id = "none";
        }

        changeVisibility(id.equals("none") ? View.INVISIBLE : View.VISIBLE);
    }

    public void changeVisibility(int visibility) {
        if(visibility != View.INVISIBLE){
            btnSalvarRecompensa.setText("Salvar");
        }else{
            btnSalvarRecompensa.setText(VERIFICAR);
        }
    }

    public void buttonSalvarClicked(View view) {
       String isVerificar = btnSalvarRecompensa.getText().toString();
       if(isVerificar.equals(VERIFICAR)){
           verificarExistencia();
           oldLevel = editTextLvlAdquire.getText().toString();
       }else{
           String level = editTextLvlAdquire.getText().toString();
           if(!level.equals(oldLevel)){
               verificarExistencia();
               oldLevel = level;
           }else{
               if (id.equals("none")) {
                   DocumentReference ref = firestore.collection("recompensas").document();
                   id = ref.getId();
               }

               if(mAwesomeValidation.validate()){
                   Recompensa recompensa = new Recompensa(id, idInstituicao, editTextDescricaoDisciplina.getText().toString(),
                           Integer.valueOf(editTextLvlAdquire.getText().toString()));

                   criarRecompensa(recompensa);
               }else {
                   Toast.makeText(getApplicationContext(), "Preencha os campos Obrigatorios",Toast.LENGTH_LONG).show();
               }
           }

       }

    }

    public void verificarExistencia(){
        Integer level = Integer.valueOf(editTextLvlAdquire.getText().toString());

        if(!level.equals("")){
            firestore.collection("recompensas").whereEqualTo("idInstituicao", idInstituicao).whereEqualTo("levelAdquire", level).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Recompensa> lista = task.getResult().toObjects(Recompensa.class);
                            if (lista.size() == 0)
                                changeVisibility(View.VISIBLE);
                            else {
                                Toast.makeText(getApplicationContext(), "Recompensa " + level + " já cadastrada.", Toast.LENGTH_LONG).show();
                                changeVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Informe o seu level", Toast.LENGTH_LONG).show();
        }
    }

    void criarRecompensa(Recompensa recompensa) {
        firestore.collection("recompensas").
                document(recompensa.getId()).set(recompensa);

        firestore.collection("recompensas").
                document(recompensa.getId()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Recompensa rec = value.toObject(Recompensa.class);
                            Toast.makeText(getApplicationContext(), "Recompensas "+ (rec != null ? rec.getDescricao() : null) +
                                    " adicionada com sucesso ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Intent i = new Intent(CadastrarRecompensaActivity.this, RecompensaActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CadastrarRecompensaActivity.this, RecompensaActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}