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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.InstituicaoDataModel;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

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

        InstituicaoDataModel.getInstance().setContext(HomeActivity.this);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        listarInstituicoes();

        adapter = new InstituicaoAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setClickListener((position, view) -> {
            Intent intent = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();
                        Instituicao from = InstituicaoDataModel.getInstance().getInstituicoes().get(fromPos);
                        Instituicao to = InstituicaoDataModel.getInstance().getInstituicoes().get(toPos);

                        InstituicaoDataModel.getInstance().getInstituicoes().set(fromPos, to);
                        InstituicaoDataModel.getInstance().getInstituicoes().set(toPos, from);
                        adapter.notifyItemMoved(fromPos,toPos);
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Instituicao i = InstituicaoDataModel.getInstance().getInstituicoes().get(position);

                        InstituicaoDataModel.getInstance().getInstituicoes().remove(i);

//                        ref.setValue(InstituicaoDataModel.getInstance().getInstituicoes())
//                                .addOnCompleteListener(task -> {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(getApplicationContext(), "Item removido com sucesso", Toast.LENGTH_LONG).show();
//                                    }
//                                });

                        adapter.notifyItemRemoved(position);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);

//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                InstituicaoDataModel.getInstance().getInstituicoes().clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Instituicao colecao = dataSnapshot.getValue(Instituicao.class);
//                    InstituicaoDataModel.getInstance().getInstituicoes().add(colecao);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarInstituicoes();
    }

    public void btnAddInstituicaoClicked(View view) {
        Intent i = new Intent(HomeActivity.this, CadastrarInstituicaoActivity.class);
        startActivity(i);
    }

    public void listarInstituicoes(){
        InstituicaoDataModel.getInstance().getInstituicoes().clear();
        firestore.collection("instituicoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Instituicao i = document.toObject(Instituicao.class);
                                Log.d("Inst ", i.toString());
                                InstituicaoDataModel.getInstance().getInstituicoes().add(i);
                            }
                        } else {
                            Log.d("Error getting documents ", task.getException().toString());
                        }
                    }
                });
    }
}