package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Turma;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfessorTurmaAdapter extends RecyclerView.Adapter<ProfessorTurmaAdapter.ProfessorTurmaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Turma> turmas;

    public ProfessorTurmaAdapter(OnListItemClick onListItemClick, List<Turma> turmas) {
        this.onListItemClick = onListItemClick;
        this.turmas = turmas;
    }

    @NonNull
    @Override
    public ProfessorTurmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma, parent, false);
        return new ProfessorTurmaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorTurmaViewHolder holder, int position) {
        holder.textViewNomeTurma.setText(turmas.get(position).getNomeTurma());
    }

    @Override
    public int getItemCount() {
        return turmas.size();
    }

    public class ProfessorTurmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeTurma;

        public ProfessorTurmaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeTurma = itemView.findViewById(R.id.textViewNomeTurma);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(turmas.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(turmas.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Turma turma, int posicao);
    }
}
