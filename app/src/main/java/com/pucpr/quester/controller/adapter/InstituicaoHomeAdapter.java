package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InstituicaoHomeAdapter extends RecyclerView.Adapter<InstituicaoHomeAdapter.InstituicaoHomeViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Instituicao> instituicoes;

    public InstituicaoHomeAdapter(OnListItemClick onListItemClick, List<Instituicao> instituicoes) {
        this.onListItemClick = onListItemClick;
        this.instituicoes = instituicoes;
    }

    @NonNull
    @Override
    public InstituicaoHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_instituicao, parent, false);
        return new InstituicaoHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstituicaoHomeViewHolder holder, int position) {
        holder.textViewNomeInstituicao.setText(instituicoes.get(position).getNome());
        holder.textViewNomeEstado.setText(instituicoes.get(position).getEstado());
        holder.textViewNomeCidade.setText(instituicoes.get(position).getCidade());
    }

    @Override
    public int getItemCount() {
        return instituicoes.size();
    }

    public class InstituicaoHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeInstituicao;
        private final TextView textViewNomeEstado;
        private final TextView textViewNomeCidade;

        public InstituicaoHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeInstituicao = itemView.findViewById(R.id.textViewNomeInstituicao);
            textViewNomeEstado = itemView.findViewById(R.id.textViewNomeEstado);
            textViewNomeCidade = itemView.findViewById(R.id.textViewNomeCidade);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(instituicoes.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(instituicoes.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Instituicao instituicao, int posicao);
    }
}
