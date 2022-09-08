package com.pucpr.quester.controller.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TurmaDisciplina2Adapter extends FirestorePagingAdapter<Disciplina, TurmaDisciplina2Adapter.TurmaDisciplinaViewHolder> {

    private final OnListItemClick onListItemClick;

    public TurmaDisciplina2Adapter(@NonNull FirestorePagingOptions<Disciplina> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull TurmaDisciplinaViewHolder holder, int position, @NonNull Disciplina model) {
        holder.textViewNomeDisciplina.setText(model.getNome());
//        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0?Color.parseColor("#00648B"):Color.parseColor("#00B0FF"));
    }

    @NonNull
    @Override
    public TurmaDisciplinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma_disciplina2, parent, false);
        return new TurmaDisciplinaViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Dados iniciais");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Carregando próxima página");
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "Todos os dados carregados");
                break;
            case ERROR:
                Log.d("PAGING_LOG", "Erro ao carregar os dados");
                break;
            case LOADED:
                Log.d("PAGING_LOG", "Total de itens carregados: "+getItemCount());
                break;
        }
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
                    onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot, int posicao);
    }
}
