package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Turma;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AlunoTurmaAdapter extends RecyclerView.Adapter<AlunoTurmaAdapter.AlunoTurmaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Turma> turmas;

    public AlunoTurmaAdapter(OnListItemClick onListItemClick, List<Turma> turmas) {
        this.onListItemClick = onListItemClick;
        this.turmas = turmas;
    }

    @NonNull
    @Override
    public AlunoTurmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma, parent, false);
        return new AlunoTurmaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoTurmaViewHolder holder, int position) {
        holder.textViewNomeTurma.setText(turmas.get(position).getNomeTurma());

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewNomeTurma.setTextColor(position % 2 == 0? Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));

    }

    @Override
    public int getItemCount() {
        return turmas.size();
    }

    public class AlunoTurmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeTurma;
        private final CardView cardViewUserView;

        public AlunoTurmaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeTurma = itemView.findViewById(R.id.textViewNomeTurma);
            cardViewUserView = itemView.findViewById(R.id.userView);

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
