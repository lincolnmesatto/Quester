package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;

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

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
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

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_CLICK", "Item clicado: "+posicao+ " ID = "+snapshot.getId());
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        i.putExtra("id", snapshot.getId());
        startActivity(i);
    }
}