package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.Questionario;
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionarioActivity extends AppCompatActivity implements QuestionarioListAdapter.OnListItemClick {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> disciplinas;
    ArrayList<Disciplina> disciplinasList;
    ArrayList<Questionario> questionarios;

    String idProfessor;
    String idInstituicao;
    String idTurma;

    RecyclerView recyclerviewListQuestionario;
    QuestionarioListAdapter questionarioListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionario);

        setTitle("Questionários");
        recyclerviewListQuestionario = findViewById(R.id.recyclerviewListQuestionario);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        disciplinas = getIntent().getStringArrayListExtra("disciplinas");
        disciplinasList = new ArrayList<>();
        questionarios = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idProfessor = extras.getString("idProfessor");
            idInstituicao = extras.getString("idInstituicao");
            idTurma = extras.getString("idTurma");
        }

        for (String disc : disciplinas) {
            popularListaDisciplina(disc);
        }

        popularListaQuestionario(idProfessor, idTurma);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
                        int position = viewHolder.getAdapterPosition();

                        deletar(position);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerviewListQuestionario);
    }

    private void popularListaQuestionario(String idProfessor, String idTurma) {
        Query ref = firestore.collection("questionarios").whereEqualTo("idProfessor", idProfessor).whereEqualTo("idTurma", idTurma);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarQuestionario(task);
                }
            }
        });
    }

    private void buscarQuestionario(Task<QuerySnapshot> task) {
        List<Questionario> qs = Objects.requireNonNull(task.getResult().toObjects(Questionario.class));
        questionarios.addAll(qs);

        questionarioListAdapter = new QuestionarioListAdapter(this, questionarios, disciplinasList);

        recyclerviewListQuestionario.setHasFixedSize(true);
        recyclerviewListQuestionario.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListQuestionario.setAdapter(questionarioListAdapter);
    }

    public void btnAddQuestionarioClicked(View view){
        Intent intent = new Intent(QuestionarioActivity.this, CadastrarQuestionarioActivity.class);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) disciplinas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idProfessor", idProfessor);
        intent.putExtra("idTurma", idTurma);
        intent.putExtra("idQuestionario", "none");
        startActivity(intent);
    }

    private void popularListaDisciplina(String disc) {
        Query ref = firestore.collection("disciplinas").whereEqualTo("id", disc);

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

    private void buscarDisciplina(Task<QuerySnapshot> task){
        List<Disciplina> ds = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
        Disciplina d = ds.get(0);
        disciplinasList.add(d);
    }

    @Override
    public void onItemClick(Questionario questionario, int posicao) {
        Intent intent = new Intent(QuestionarioActivity.this, CadastrarQuestionarioActivity.class);
        intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) disciplinas);
        intent.putExtra("idInstituicao", idInstituicao);
        intent.putExtra("idProfessor", idProfessor);
        intent.putExtra("idTurma", idTurma);
        intent.putExtra("idQuestionario", questionario.getId());
        startActivity(intent);
    }

    public void deletar(int position){
        firestore.collection("questionarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Questionario> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.toObject(Questionario.class));
                    }
                    Questionario q = list.get(position);
                    firestore.collection("questionarios").document(q.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    questionarioListAdapter.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "Item removido com sucesso", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(QuestionarioActivity.this, QuestionarioActivity.class);
                                    intent.putStringArrayListExtra("disciplinas", (ArrayList<String>) disciplinas);
                                    intent.putExtra("idInstituicao", idInstituicao);
                                    intent.putExtra("idProfessor", idProfessor);
                                    intent.putExtra("idTurma", idTurma);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("EXCLUIR_QUESTIONARIO", "Erro ao exlcuir questionárioo", e);
                                }
                            });
                } else {
                    Log.d("OBTER_QUESTIONARIO", "Erro ao obter: ", task.getException());
                }
            }
        });
    }
}