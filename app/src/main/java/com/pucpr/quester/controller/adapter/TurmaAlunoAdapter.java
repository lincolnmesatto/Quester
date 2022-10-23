package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaAlunoAdapter extends RecyclerView.Adapter<TurmaAlunoAdapter.TurmaAlunoViewHolder> {

    private final OnListItemClick onListItemClick;

    List<UsuarioAlunoModel> alunos;

    public TurmaAlunoAdapter(OnListItemClick onListItemClick, List<UsuarioAlunoModel> alunos) {
        this.onListItemClick = onListItemClick;
        this.alunos = alunos;
    }

    @NonNull
    @Override
    public TurmaAlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_aluno, parent, false);
        return new TurmaAlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaAlunoViewHolder holder, int position) {
        holder.textViewNomeAluno.setText(alunos.get(position).getUsuario().getNome());
        holder.textViewCpfAluno.setText(alunos.get(position).getUsuario().getCpf());

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewCpfAluno.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewNomeAluno.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));

    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public class TurmaAlunoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeAluno;
        private final TextView textViewCpfAluno;
        private final CardView cardViewUserView;

        public TurmaAlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeAluno = itemView.findViewById(R.id.textViewNomeAluno);
            textViewCpfAluno = itemView.findViewById(R.id.textViewCpfAluno);
            cardViewUserView = itemView.findViewById(R.id.userView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(alunos.get(getAdapterPosition()).getAluno(), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(alunos.get(getAdapterPosition()).getAluno(), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Aluno aluno, int posicao);
    }
}
