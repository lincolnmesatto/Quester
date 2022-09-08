package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.UsuarioProfessorModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaProfessor2Adapter extends RecyclerView.Adapter<TurmaProfessor2Adapter.TurmaProfessor2ViewHolder> {

    List<UsuarioProfessorModel> professores;

    public TurmaProfessor2Adapter(List<UsuarioProfessorModel> professores) {
        this.professores = professores;
    }

    @NonNull
    @Override
    public TurmaProfessor2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_professor2, parent, false);
        return new TurmaProfessor2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaProfessor2ViewHolder holder, int position) {
        holder.textViewNomeProfessor.setText(professores.get(position).getUsuario().getNome());
        holder.textViewCpfProfessor.setText(professores.get(position).getUsuario().getCpf());
    }

    @Override
    public int getItemCount() {
        return professores.size();
    }

    public class TurmaProfessor2ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNomeProfessor;
        private final TextView textViewCpfProfessor;

        public TurmaProfessor2ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeProfessor = itemView.findViewById(R.id.textViewNomeProfessor);
            textViewCpfProfessor = itemView.findViewById(R.id.textViewCpfProfessor);
        }
    }
}
