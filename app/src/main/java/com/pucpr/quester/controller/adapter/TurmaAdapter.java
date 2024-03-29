package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Turma;

public class TurmaAdapter extends FirestorePagingAdapter<Turma, TurmaAdapter.TurmaViewHolder> {

    private final OnListItemClick onListItemClick;

    public TurmaAdapter(@NonNull FirestorePagingOptions<Turma> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull TurmaViewHolder holder, int position, @NonNull Turma model) {
        holder.textViewNomeTurma.setText(model.getNomeTurma());

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewNomeTurma.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
    }

    @NonNull
    @Override
    public TurmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_turma, parent, false);
        return new TurmaViewHolder(view);
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

    public class TurmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeTurma;
        private final CardView cardViewUserView;

        public TurmaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeTurma = itemView.findViewById(R.id.textViewNomeTurma);
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
