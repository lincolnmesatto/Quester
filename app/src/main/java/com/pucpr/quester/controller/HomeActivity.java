package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;

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

        Query query =  firestore.collection("instituicoes");
        FirestoreRecyclerOptions<Instituicao> options = new FirestoreRecyclerOptions.Builder<Instituicao>()
                .setQuery(query, Instituicao.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Instituicao, InstituicaoViewHolder>(options) {
            @NonNull
            @Override
            public InstituicaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_instituicao, parent, false);
                return new InstituicaoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull InstituicaoViewHolder holder, int position, @NonNull Instituicao model) {
                holder.textViewName.setText(model.getNome());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private class InstituicaoViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;

        public InstituicaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void btnAddInstituicaoClicked(View view) {
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        startActivity(i);
    }
}