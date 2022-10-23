package com.pucpr.quester.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.Usuario;
import com.pucpr.quester.model.UsuarioProfessorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RedirectLoginActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String uid = firebaseUser.getUid();

        Query query =  firestore.collection("usuarios").whereEqualTo("idUsuario", uid);
        Task<QuerySnapshot> t = query.get();

        t.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    redirecionar(task);
                }
            }
        });
    }

    public void redirecionar(Task<QuerySnapshot> t){
        List<Usuario> users = Objects.requireNonNull(t.getResult().toObjects(Usuario.class));

        for (Usuario user: users) {
           switch (user.getPerfil()){
               case 1:
                   Intent i = new Intent(RedirectLoginActivity.this, HomeAlunoActivity.class);
                   startActivity(i);
                   break;
               case 2:
                   Intent intent = new Intent(RedirectLoginActivity.this, HomeProfessorActivity.class);
                   startActivity(intent);
                   break;
               case 3:
                   Intent i2 = new Intent(RedirectLoginActivity.this, MenuAdminActivity.class);
//                   i.putExtra("id", id);
                   startActivity(i2);
                   break;
               default:
                   break;
           }
        }
    }
}