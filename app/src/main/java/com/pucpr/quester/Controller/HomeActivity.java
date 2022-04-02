package com.pucpr.quester.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.pucpr.quester.Model.Instituicao;
import com.pucpr.quester.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firestore = FirebaseFirestore.getInstance();
    }

    public void buttonLoginClicked(View view){
        createDataOnFirestore();
    }

    void createDataOnFirestore(){
        Instituicao instituicao = new Instituicao("3", "Acesso", "Paraná", "Curitiba");
        firestore.collection("insituicoes").
                document(instituicao.getId()+"-"+instituicao.getNome()).set(instituicao);

        firestore.collection("insituicoes").
                document(instituicao.getId()+"-"+instituicao.getNome()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            error.printStackTrace();
                            return;
                        }
                        if(value != null && value.exists()){
                            Instituicao instituicao = value.toObject(Instituicao.class);
                            Log.d("Firestore",instituicao.getNome());
                        }
                    }
                });
    }
}