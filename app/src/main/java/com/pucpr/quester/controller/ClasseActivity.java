package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.ClasseAdapter;
import com.pucpr.quester.controller.adapter.QuestionarioListAdapter;
import com.pucpr.quester.model.Classe;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questionario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClasseActivity extends AppCompatActivity implements ClasseAdapter.OnListItemClick  {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    List<Disciplina> disciplinasList;
    ArrayList<Classe> classesList;

    RecyclerView recyclerviewListClasse;
    ClasseAdapter classeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe);

        setTitle("Classe");
        recyclerviewListClasse = findViewById(R.id.recyclerViewClasse);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        disciplinasList = new ArrayList<>();
        classesList = new ArrayList<>();

        popularListaDisciplina();
        popularListaClasse();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerviewListClasse);

    }

    private void popularListaClasse() {
        Query ref = firestore.collection("classes");

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    buscarClasse(task);
                }
            }
        });

    }
    private void buscarClasse(Task<QuerySnapshot> task) {
        List<Classe> cs = Objects.requireNonNull(task.getResult().toObjects(Classe.class));
        classesList.addAll(cs);

        classeAdapter = new ClasseAdapter(this, disciplinasList, classesList);

        recyclerviewListClasse.setHasFixedSize(true);
        recyclerviewListClasse.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListClasse.setAdapter(classeAdapter);
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
        disciplinasList = Objects.requireNonNull(task.getResult().toObjects(Disciplina.class));
    }

    @Override
    public void onItemClick(Classe classe, int posicao) {
        Intent intent = new Intent(ClasseActivity.this, CadastrarClasseActivity.class);
        intent.putExtra("id", classe.getId());
        startActivity(intent);
    }

    public void buttonAddClasseClick(View view){
        Intent intent = new Intent(ClasseActivity.this, CadastrarClasseActivity.class);
        startActivity(intent);
    }
}