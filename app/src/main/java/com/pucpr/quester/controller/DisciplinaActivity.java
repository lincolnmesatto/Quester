package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.DisciplinaAdapter;
import com.pucpr.quester.model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaActivity extends AppCompatActivity implements DisciplinaAdapter.OnListItemClick {

    RecyclerView recyclerViewDisciplina;
    DisciplinaAdapter disciplinaAdapter;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        setTitle("Disciplina");
        recyclerViewDisciplina = findViewById(R.id.recyclerViewDisciplina);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        popularRecyclerView();

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
        itemTouchHelper.attachToRecyclerView(recyclerViewDisciplina);
    }

    public void popularRecyclerView(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("disciplinas");
        FirestorePagingOptions<Disciplina> options = new FirestorePagingOptions.Builder<Disciplina>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Disciplina>() {
                    @NonNull
                    @Override
                    public Disciplina parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Disciplina d = snapshot.toObject(Disciplina.class);
                        d.setId(snapshot.getId());
                        return d;
                    }
                })
                .build();

        disciplinaAdapter = new DisciplinaAdapter(options, this);

        recyclerViewDisciplina.setHasFixedSize(true);
        recyclerViewDisciplina.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDisciplina.setAdapter(disciplinaAdapter);
    }

    public void btnAddDisciplinaClicked(View view) {
        Intent i = new Intent(DisciplinaActivity.this, CadastrarDisciplinaActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(DisciplinaActivity.this, CadastrarDisciplinaActivity.class);
        i.putExtra("id", snapshot.getId());
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_LONG_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(DisciplinaActivity.this, CadastrarDisciplinaActivity.class);
        i.putExtra("id_disciplina", snapshot.getId());
        Disciplina disciplina = snapshot.toObject(Disciplina.class);
        i.putExtra("nome_disciplina", disciplina.getNome());
        startActivity(i);

        return true;
    }

    public void deletar(int position){
        firestore.collection("disciplinas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Disciplina> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.toObject(Disciplina.class));
                    }
                    Disciplina d = list.get(position);
                    firestore.collection("disciplinas").document(d.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    disciplinaAdapter.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "Item removido com sucesso", Toast.LENGTH_LONG).show();

                                    popularRecyclerView();
                                }
                            })
                            .addOnFailureListener(e -> {
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DisciplinaActivity.this, HomeActivity.class);
        startActivity(i);
    }
}