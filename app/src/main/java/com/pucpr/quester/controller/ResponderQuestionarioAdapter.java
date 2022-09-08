package com.pucpr.quester.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.DataModelResposta;
import com.pucpr.quester.model.Questao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResponderQuestionarioAdapter extends RecyclerView.Adapter<ResponderQuestionarioAdapter.ResponderQuestionarioViewHolder> implements ResponderAlternativaAdapter.OnListItemClick {

    private final OnListItemClick onListItemClick;

    List<Questao> questoes;
    ResponderAlternativaAdapter responderAlternativaAdapter;

    public ResponderQuestionarioAdapter(OnListItemClick onListItemClick, List<Questao> questoes) {
        this.onListItemClick = onListItemClick;
        this.questoes = questoes;
    }

    @NonNull
    @Override
    public ResponderQuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_questoes_resposta, parent, false);
        return new ResponderQuestionarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponderQuestionarioViewHolder holder, int position) {
        holder.tvEnunciadoResposta.setText(DataModelResposta.getInstance().getQuestoesDataModel().get(holder.getAdapterPosition()).getEnunciado());
        responderAlternativaAdapter = new ResponderAlternativaAdapter(this, questoes.get(holder.getAdapterPosition()).getAlternativas(), holder.getAdapterPosition());

        holder.recyclerViewAlternativaResposta.setHasFixedSize(true);
        holder.recyclerViewAlternativaResposta.setLayoutManager(new LinearLayoutManager(holder.recyclerViewAlternativaResposta.getContext()));
        holder.recyclerViewAlternativaResposta.setAdapter(responderAlternativaAdapter);
    }

    @Override
    public int getItemCount() {
        return questoes.size();
    }

    @Override
    public void onItemClick(Alternativa alternativa, int posicao) {

    }

    public class ResponderQuestionarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvEnunciadoResposta;
        private final RecyclerView recyclerViewAlternativaResposta;

        public ResponderQuestionarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnunciadoResposta = itemView.findViewById(R.id.tvEnunciadoResposta);
            recyclerViewAlternativaResposta = itemView.findViewById(R.id.recyclerViewAlternativaResposta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(questoes.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(questoes.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Questao questao, int posicao);
    }
}
