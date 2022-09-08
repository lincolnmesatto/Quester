package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaDisciplinaAdapter extends RecyclerView.Adapter<TurmaDisciplinaAdapter.TurmaDisciplinaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Disciplina> disciplinas;

    public TurmaDisciplinaAdapter(OnListItemClick onListItemClick, List<Disciplina> disciplinas) {
        this.onListItemClick = onListItemClick;
        this.disciplinas = disciplinas;
    }

    @NonNull
    @Override
    public TurmaDisciplinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_disciplina, parent, false);
        return new TurmaDisciplinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaDisciplinaViewHolder holder, int position) {
        holder.textViewNomeDisciplina.setText(disciplinas.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

    public class TurmaDisciplinaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeDisciplina;

        public TurmaDisciplinaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeDisciplina = itemView.findViewById(R.id.textViewNomeDisciplina);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(disciplinas.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(disciplinas.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Disciplina disciplina, int posicao);
    }
}
