package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Classe;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Turma;

import java.util.List;

public class ClasseAdapter extends RecyclerView.Adapter<ClasseAdapter.ClasseViewHolder>{

    private final OnListItemClick onListItemClick;
    List<Disciplina> disciplinas;
    List<Classe> classes;

    public ClasseAdapter(ClasseAdapter.OnListItemClick onListItemClick, List<Disciplina> disciplinas, List<Classe> classes) {
        this.onListItemClick = onListItemClick;
        this.disciplinas = disciplinas;
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClasseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_classe, parent, false);
        return new ClasseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClasseViewHolder holder, int position) {
        holder.textViewClasse.setText(classes.get(position).getNome());
        for (Disciplina d : disciplinas) {
            if(classes.get(position).getIdDisciplina().equals(d.getId())){
                holder.textViewClasseDisciplina.setText(d.getNome());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public class ClasseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewClasse;
        private final TextView textViewClasseDisciplina;

        public ClasseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClasse = itemView.findViewById(R.id.textViewClasse);
            textViewClasseDisciplina = itemView.findViewById(R.id.textViewClasseDisciplina);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(classes.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(classes.get(getAdapterPosition()), getAdapterPosition());
        }
    }


    public interface OnListItemClick {
        void onItemClick(Classe classe, int posicao);
    }

}
