package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Professor;
import com.pucpr.quester.model.UsuarioProfessorModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaProfessorAdapter extends RecyclerView.Adapter<TurmaProfessorAdapter.TurmaProfessorViewHolder> {

    private final OnListItemClick onListItemClick;

    List<UsuarioProfessorModel> professores;

    public TurmaProfessorAdapter(OnListItemClick onListItemClick, List<UsuarioProfessorModel> professores) {
        this.onListItemClick = onListItemClick;
        this.professores = professores;
    }

    @NonNull
    @Override
    public TurmaProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_professor, parent, false);
        return new TurmaProfessorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaProfessorViewHolder holder, int position) {
        holder.textViewNomeProfessor.setText(professores.get(position).getUsuario().getNome());
        holder.textViewCpfProfessor.setText(professores.get(position).getUsuario().getCpf());
    }

    @Override
    public int getItemCount() {
        return professores.size();
    }

    public class TurmaProfessorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeProfessor;
        private final TextView textViewCpfProfessor;

        public TurmaProfessorViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeProfessor = itemView.findViewById(R.id.textViewNomeProfessor);
            textViewCpfProfessor = itemView.findViewById(R.id.textViewCpfProfessor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(professores.get(getAdapterPosition()).getProfessor(), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(professores.get(getAdapterPosition()).getProfessor(), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Professor professor, int posicao);
    }
}
