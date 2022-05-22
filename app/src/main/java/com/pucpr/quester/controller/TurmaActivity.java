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
import com.pucpr.quester.model.Turma;

import java.util.ArrayList;
import java.util.List;

public class TurmaActivity extends AppCompatActivity implements TurmaAdapter.OnListItemClick{

    RecyclerView recyclerViewTurma;
    TurmaAdapter turmaAdapter;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    String idInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma);

        setTitle("Turma");
        recyclerViewTurma = findViewById(R.id.recyclerViewTurma);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idInstituicao = extras.getString("id_instituicao");
        }

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
        itemTouchHelper.attachToRecyclerView(recyclerViewTurma);
    }
    public void popularRecyclerView(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("turmas").whereEqualTo("idInstituicao", idInstituicao);
        FirestorePagingOptions<Turma> options = new FirestorePagingOptions.Builder<Turma>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Turma>() {
                    @NonNull
                    @Override
                    public Turma parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Turma t = snapshot.toObject(Turma.class);
                        t.setId(snapshot.getId());
                        return t;
                    }
                })
                .build();

        turmaAdapter = new TurmaAdapter(options, this);

        recyclerViewTurma.setHasFixedSize(true);
        recyclerViewTurma.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTurma.setAdapter(turmaAdapter);
    }
    public void btnAddTurmaClicked(View view) {
        Intent i = new Intent(TurmaActivity.this, CadastrarTurmaActivity.class);
        i.putExtra("id", "none");
        i.putExtra("id_instituicao", idInstituicao);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(TurmaActivity.this, CadastrarTurmaActivity.class);
        i.putExtra("id", snapshot.getId());
        i.putExtra("id_instituicao", idInstituicao);
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(DocumentSnapshot snapshot, int posicao) {
        return true;
    }

    public void deletar(int position){
        firestore.collection("turmas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Turma> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.toObject(Turma.class));
                    }
                    Turma t = list.get(position);
                    firestore.collection("turmas").document(t.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    turmaAdapter.notifyItemRemoved(position);
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
}