package com.pucpr.quester.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.Questao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlternativaAdapter extends RecyclerView.Adapter<AlternativaAdapter.AlternativaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Alternativa> alternativas;

    public AlternativaAdapter(OnListItemClick onListItemClick, List<Alternativa> alternativas) {
        this.onListItemClick = onListItemClick;
        this.alternativas = alternativas;
    }

    @NonNull
    @Override
    public AlternativaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_alternativas, parent, false);
        return new AlternativaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativaViewHolder holder, int position) {
        //TODO setar o layout do subitem
        holder.editTextAlternativas.setText(alternativas.get(position).getAlternativa());
    }

    @Override
    public int getItemCount() {
        return alternativas.size();
    }

    public class AlternativaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView editTextAlternativas;
//        private final TextView textViewCpfAluno;

        public AlternativaViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextAlternativas = itemView.findViewById(R.id.editTextAlternativas);
//            textViewCpfAluno = itemView.findViewById(R.id.textViewCpfAluno);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(alternativas.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(alternativas.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Alternativa alternativa, int posicao);
    }
}
