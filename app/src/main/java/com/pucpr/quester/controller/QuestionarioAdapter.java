package com.pucpr.quester.controller;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.Aluno;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.Questao;
import com.pucpr.quester.model.Questionario;
import com.pucpr.quester.model.UsuarioAlunoModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder> implements AlternativaAdapter.OnListItemClick {

    private final OnListItemClick onListItemClick;

    List<Questao> questoes;
    AlternativaAdapter alternativaAdapter;

    public QuestionarioAdapter(OnListItemClick onListItemClick, List<Questao> questoes) {
        this.onListItemClick = onListItemClick;
        this.questoes = questoes;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_questoes, parent, false);
        return new QuestionarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        alternativaAdapter = new AlternativaAdapter(this, questoes.get(holder.getAdapterPosition()).getAlternativas(), holder.getAdapterPosition());

        holder.recyclerViewAlternativa.setHasFixedSize(true);
        holder.recyclerViewAlternativa.setLayoutManager(new LinearLayoutManager(holder.recyclerViewAlternativa.getContext()));
        holder.recyclerViewAlternativa.setAdapter(alternativaAdapter);

        holder.editTextQuestao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questoes.get(holder.getAdapterPosition()).setEnunciado(holder.editTextQuestao.getText().toString());
                DataModel.getInstance().getQuestoesDataModel().get(holder.getAdapterPosition()).setEnunciado(holder.editTextQuestao.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return questoes.size();
    }

    @Override
    public void onItemClick(Alternativa alternativa, int posicao) {

    }

    public class QuestionarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView editTextQuestao;
        private final RecyclerView recyclerViewAlternativa;

        public QuestionarioViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextQuestao = itemView.findViewById(R.id.editTextQuestao);
            recyclerViewAlternativa = itemView.findViewById(R.id.recyclerViewAlternativa);

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
