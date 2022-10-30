package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.Recompensa;
import com.pucpr.quester.model.RecompensaAluno;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecompensaAlunoAdapter extends RecyclerView.Adapter<RecompensaAlunoAdapter.RecompensaAlunoViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Recompensa> recompensas;
    List<RecompensaAluno> recompensaAlunos;

    public RecompensaAlunoAdapter(OnListItemClick onListItemClick, List<Recompensa> recompensas, List<RecompensaAluno> recompensaAlunos) {
        this.onListItemClick = onListItemClick;
        this.recompensas = recompensas;
        this.recompensaAlunos = recompensaAlunos;
    }

    @NonNull
    @Override
    public RecompensaAlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recompensa_aluno, parent, false);
        return new RecompensaAlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecompensaAlunoViewHolder holder, int position) {
        holder.textViewDescricaoRecompensaAluno.setText(recompensas.get(position).getDescricao());
        holder.textViewLevelRecompensaAluno.setText("Nível: " + recompensas.get(position).getLevelAdquire());
        holder.textViewObtidoRecompensaAluno.setText(recompensaAlunos.get(position).isResgatada() ? "Obtido" : "Obter");

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewDescricaoRecompensaAluno.setTextColor(position % 2 == 0? Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewLevelRecompensaAluno.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewObtidoRecompensaAluno.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
    }

    @Override
    public int getItemCount() {
        return recompensaAlunos.size();
    }

    public class RecompensaAlunoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewDescricaoRecompensaAluno;
        private final TextView textViewLevelRecompensaAluno;
        private final TextView textViewObtidoRecompensaAluno;
        private final CardView cardViewUserView;

        public RecompensaAlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescricaoRecompensaAluno = itemView.findViewById(R.id.textViewDescricaoRecompensaAluno);
            textViewLevelRecompensaAluno = itemView.findViewById(R.id.textViewLevelRecompensaAluno);
            textViewObtidoRecompensaAluno = itemView.findViewById(R.id.textViewObtidoRecompensaAluno);
            cardViewUserView = itemView.findViewById(R.id.userView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(recompensaAlunos.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(recompensaAlunos.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(RecompensaAluno recompensa, int posicao);
    }
}
