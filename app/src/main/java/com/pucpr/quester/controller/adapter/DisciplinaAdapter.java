package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DisciplinaAdapter extends FirestorePagingAdapter<Disciplina, DisciplinaAdapter.DisciplinaViewHolder> {

    private final OnListItemClick onListItemClick;

    public DisciplinaAdapter(@NonNull FirestorePagingOptions<Disciplina> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull DisciplinaViewHolder holder, int position, @NonNull Disciplina model) {
        holder.textViewNomeDisciplina.setText(model.getNome());
        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewNomeDisciplina.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
    }

    @NonNull
    @Override
    public DisciplinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_disciplina, parent, false);
        return new DisciplinaViewHolder(view);
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

    public class DisciplinaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeDisciplina;
        private final CardView cardViewUserView;

        public DisciplinaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeDisciplina = itemView.findViewById(R.id.textViewNomeDisciplina);
            cardViewUserView = itemView.findViewById(R.id.userView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(onListItemClick == null)
                        return false;

                    return onListItemClick.onItemLongClick(getItem(getAdapterPosition()), getAdapterPosition());
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
        boolean onItemLongClick(DocumentSnapshot snapshot, int posicao);
    }
}
