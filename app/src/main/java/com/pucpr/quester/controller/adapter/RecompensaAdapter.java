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
import com.pucpr.quester.model.Recompensa;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecompensaAdapter extends FirestorePagingAdapter<Recompensa, RecompensaAdapter.RecompensaViewHolder> {
    private final RecompensaAdapter.OnListItemClick onListItemClick;

    public RecompensaAdapter(@NonNull FirestorePagingOptions<Recompensa> options, RecompensaAdapter.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecompensaAdapter.RecompensaViewHolder holder, int position, @NonNull Recompensa model) {
        holder.textViewDescricaoRecompensa.setText(model.getDescricao());
        holder.textViewLvl.setText("Nível: "+model.getLevelAdquire());
//        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0?Color.parseColor("#00648B"):Color.parseColor("#00B0FF"));
    }

    @NonNull
    @Override
    public RecompensaAdapter.RecompensaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recompensa, parent, false);
        return new RecompensaAdapter.RecompensaViewHolder(view);
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

    public class RecompensaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewDescricaoRecompensa;
        private final TextView textViewLvl;

        public RecompensaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescricaoRecompensa = itemView.findViewById(R.id.textViewDescricaoRecompensa);
            textViewLvl = itemView.findViewById(R.id.textViewLvl);

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
