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
import com.pucpr.quester.model.Instituicao;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class InstituicaoAdapter extends FirestorePagingAdapter<Instituicao, InstituicaoAdapter.InstituicaoViewHolder> {

    private final OnListItemClick onListItemClick;

    public InstituicaoAdapter(@NonNull FirestorePagingOptions<Instituicao> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull InstituicaoViewHolder holder, int position, @NonNull Instituicao model) {
        holder.textViewNomeInstituicao.setText(model.getNome());
        holder.textViewNomeEstado.setText(model.getEstado());
        holder.textViewNomeCidade.setText(model.getCidade());

//        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0?Color.parseColor("#00648B"):Color.parseColor("#00B0FF"));

    }

    @NonNull
    @Override
    public InstituicaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_instituicao, parent, false);
        return new InstituicaoViewHolder(view);
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

    public class InstituicaoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewNomeInstituicao;
        private final TextView textViewNomeEstado;
        private final TextView textViewNomeCidade;
        private final CardView cardViewUserView;


        public InstituicaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeInstituicao = itemView.findViewById(R.id.textViewNomeInstituicao);
            textViewNomeEstado = itemView.findViewById(R.id.textViewNomeEstado);
            textViewNomeCidade = itemView.findViewById(R.id.textViewNomeCidade);
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
