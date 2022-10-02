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
import com.google.android.gms.tasks.OnFailureListener;
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
import com.pucpr.quester.controller.adapter.InstituicaoAdapter;
import com.pucpr.quester.model.Instituicao;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements InstituicaoAdapter.OnListItemClick {

    RecyclerView recyclerView;
    InstituicaoAdapter adapter;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Instituição");
        recyclerView = findViewById(R.id.recyclerView);

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
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void popularRecyclerView(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("instituicoes");
        FirestorePagingOptions<Instituicao> options = new FirestorePagingOptions.Builder<Instituicao>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Instituicao>() {
                    @NonNull
                    @Override
                    public Instituicao parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Instituicao i = snapshot.toObject(Instituicao.class);
                        i.setId(snapshot.getId());
                        return i;
                    }
                })
                .build();

        adapter = new InstituicaoAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void btnAddInstituicaoClicked(View view) {
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        startActivity(i);
    }

    public void btnAddDisciplinaClicked(View view) {
        Intent i = new Intent(HomeActivity.this, DisciplinaActivity.class);
        startActivity(i);
    }

    public void btnAddClasseClicked(View view) {
        Intent i = new Intent(HomeActivity.this, ClasseActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        i.putExtra("id", snapshot.getId());
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_LONG_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(HomeActivity.this, InsituicaoDerivadoActivity.class);
        i.putExtra("id_instituicao", snapshot.getId());
        Instituicao instituicao = snapshot.toObject(Instituicao.class);
        i.putExtra("nome_instituicao", instituicao.getNome());
        startActivity(i);

        return true;
    }

    public void deletar(int position){
        firestore.collection("instituicoes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Instituicao> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.toObject(Instituicao.class));
                    }
                    Instituicao i = list.get(position);
                    firestore.collection("instituicoes").document(i.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("EXCLUIR_INSTITUICAO", "Instituicao deletada com sucesso");
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(getApplicationContext(), "Item removido com sucesso", Toast.LENGTH_LONG).show();

                                    popularRecyclerView();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("EXCLUIR_INSTITUICAO", "Erro ao exlcuir instituicao", e);
                                }
                            });
                } else {
                    Log.d("OBTER_INSTITUICAO", "Erro ao obter: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //firebaseAuth.signOut();
        Intent i = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(i);
    }
}