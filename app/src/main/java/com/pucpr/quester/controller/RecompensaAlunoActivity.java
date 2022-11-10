package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.controller.adapter.RecompensaAlunoAdapter;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Recompensa;
import com.pucpr.quester.model.RecompensaAluno;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RecompensaAlunoActivity extends AppCompatActivity implements RecompensaAlunoAdapter.OnListItemClick {

    RecyclerView recyclerViewRecompensaAluno;
    RecompensaAlunoAdapter recompensaAlunoAdapter;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ArrayList<String> turmas;
    String idAluno;
    String idInstituicao;

    Aluno aluno;
    List<Recompensa> recompensas;
    List<RecompensaAluno> recompensasAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompensa_aluno);

        setTitle("Recompensas");
        recyclerViewRecompensaAluno = findViewById(R.id.recyclerViewRecompensaAluno);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        aluno = new Aluno();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            idAluno = extras.getString("idAluno");
            idInstituicao = extras.getString("idInstituicao");
            turmas = getIntent().getStringArrayListExtra("turmas");
        }

        popularAluno();

        recompensas = new ArrayList<>();
        recompensasAluno = new ArrayList<>();

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

                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerViewRecompensaAluno);

        recompensaAlunoAdapter = new RecompensaAlunoAdapter(this, recompensas, recompensasAluno);

        recyclerViewRecompensaAluno.setHasFixedSize(true);
        recyclerViewRecompensaAluno.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecompensaAluno.setAdapter(recompensaAlunoAdapter);
    }

    private void popularAluno() {
        Query ref = firestore.collection("alunos").whereEqualTo("id", idAluno);

        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarAluno(task);
                }
            }
        });
    }

    private void buscarAluno(Task<QuerySnapshot> task) {
        List<Aluno> alunos = Objects.requireNonNull(task.getResult().toObjects(Aluno.class));
        aluno = alunos.get(0);
    }

    private void popularRecyclerView() {
        Query ref = firestore.collection("recompensas").whereEqualTo("idInstituicao", idInstituicao);
        Task<QuerySnapshot> t = ref.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    buscarRecompensas(task);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buscarRecompensas(Task<QuerySnapshot> task) {
        List<Recompensa> list = Objects.requireNonNull(task.getResult().toObjects(Recompensa.class));

        for (Recompensa r : list) {
            if(r.getLevelAdquire() <= aluno.getLevel()){
                recompensas.add(r);
            }
        }

        recompensas.sort(Comparator.comparing(Recompensa::getLevelAdquire));

        popularRecompensaAluno();
    }

    private void popularRecompensaAluno() {
        for (Recompensa r : recompensas) {
            Query ref = firestore.collection("recompensasAluno").whereEqualTo("idRecompensa", r.getId())
                    .whereEqualTo("idAluno", aluno.getId());

            Task<QuerySnapshot> t = ref.get();

            t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        buscarRecompensasAluno(task, r);
                    }
                }
            });
        }
    }

    private void buscarRecompensasAluno(Task<QuerySnapshot> task, Recompensa recompensa) {
        List<RecompensaAluno> recompensasAlunoList = Objects.requireNonNull(task.getResult().toObjects(RecompensaAluno.class));

        if(recompensasAlunoList.size() > 0){
            RecompensaAluno r = recompensasAlunoList.get(0);
            recompensasAluno.add(r);
        }else{
            recompensasAluno.add(new RecompensaAluno("none", aluno.getId(), recompensa.getId(), false));
        }

        recompensaAlunoAdapter = new RecompensaAlunoAdapter(this, recompensas, recompensasAluno);

        recyclerViewRecompensaAluno.setHasFixedSize(true);
        recyclerViewRecompensaAluno.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecompensaAluno.setAdapter(recompensaAlunoAdapter);
    }

    @Override
    public void onItemClick(RecompensaAluno recompensa, int posicao) {
        RecompensaAluno ra = recompensasAluno.get(posicao);

        if(!ra.isResgatada()){
            DocumentReference ref = firestore.collection("recompensasAluno").document();
            String id = ref.getId();

            ra.setId(id);
            ra.setResgatada(true);

            firestore.collection("recompensasAluno").document(id).set(ra);

            Toast.makeText(getApplicationContext(), "Recompensa obtida com sucesso ", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RecompensaAlunoActivity.this, RecompensaAlunoActivity.class);
            intent.putStringArrayListExtra("turmas", turmas);
            intent.putExtra("idInstituicao", idInstituicao);
            intent.putExtra("idAluno", idAluno);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RecompensaAlunoActivity.this, AlunoInstituicaoDerivadoActivity.class);
        i.putExtra("idInstituicao", idInstituicao);
        i.putExtra("idAluno", idAluno);
        i.putExtra("turmas", turmas);
        startActivity(i);
    }
}