package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.pucpr.quester.controller.adapter.RecompensaAdapter;
import com.pucpr.quester.model.Recompensa;

import java.util.ArrayList;
import java.util.List;

public class RecompensaActivity extends AppCompatActivity implements RecompensaAdapter.OnListItemClick {

    RecyclerView recyclerViewRecompensa;
    RecompensaAdapter recompensaAdapter;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    String idInstituicao;
    String nomeInstituicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompensa);

        setTitle("Recompensa");
        recyclerViewRecompensa = findViewById(R.id.recyclerViewRecompensa);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idInstituicao = extras.getString("id_instituicao");
            nomeInstituicao = extras.getString("nome_instituicao");
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
        itemTouchHelper.attachToRecyclerView(recyclerViewRecompensa);
    }

    public void popularRecyclerView(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        Query query =  firestore.collection("recompensas").whereEqualTo("idInstituicao", idInstituicao);
        FirestorePagingOptions<Recompensa> options = new FirestorePagingOptions.Builder<Recompensa>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Recompensa>() {
                    @NonNull
                    @Override
                    public Recompensa parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Recompensa r = snapshot.toObject(Recompensa.class);
                        r.setId(snapshot.getId());
                        return r;
                    }
                })
                .build();

        recompensaAdapter = new RecompensaAdapter(options, this);

        recyclerViewRecompensa.setHasFixedSize(true);
        recyclerViewRecompensa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecompensa.setAdapter(recompensaAdapter);
    }

    public void btnAddRecompensaClicked(View view) {
        Intent i = new Intent(RecompensaActivity.this, CadastrarRecompensaActivity.class);
        i.putExtra("id", "none");
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Intent i = new Intent(RecompensaActivity.this, CadastrarRecompensaActivity.class);
        i.putExtra("id", snapshot.getId());
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }

    public void deletar(int position){
        firestore.collection("recompensas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Recompensa> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.toObject(Recompensa.class));
                    }
                    Recompensa d = list.get(position);
                    firestore.collection("recompensas").document(d.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    recompensaAdapter.notifyItemRemoved(position);
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
        Intent i = new Intent(RecompensaActivity.this, InsituicaoDerivadoActivity.class);
        i.putExtra("id_instituicao", idInstituicao);
        i.putExtra("nome_instituicao", nomeInstituicao);
        startActivity(i);
    }
}