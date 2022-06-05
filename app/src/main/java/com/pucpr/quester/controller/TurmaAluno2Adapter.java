package com.pucpr.quester.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaAluno2Adapter extends RecyclerView.Adapter<TurmaAluno2Adapter.TurmaAluno2ViewHolder> {

    List<UsuarioAlunoModel> alunos;

    public TurmaAluno2Adapter(List<UsuarioAlunoModel> alunos) {
        this.alunos = alunos;
    }

    @NonNull
    @Override
    public TurmaAluno2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_aluno2, parent, false);
        return new TurmaAluno2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaAluno2ViewHolder holder, int position) {
        holder.textViewNomeAluno.setText(alunos.get(position).getUsuario().getNome());
        holder.textViewCpfAluno.setText(alunos.get(position).getUsuario().getCpf());
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public class TurmaAluno2ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNomeAluno;
        private final TextView textViewCpfAluno;

        public TurmaAluno2ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeAluno = itemView.findViewById(R.id.textViewNomeAluno);
            textViewCpfAluno = itemView.findViewById(R.id.textViewCpfAluno);
        }
    }
}
