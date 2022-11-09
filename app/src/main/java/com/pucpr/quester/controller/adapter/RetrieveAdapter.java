package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Arquivo;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questionario;

import java.util.List;

public class RetrieveAdapter extends RecyclerView.Adapter<RetrieveAdapter.RetrieveViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Arquivo> arquivos;

    public RetrieveAdapter(OnListItemClick onListItemClick, List<Arquivo> arquivos) {
        this.onListItemClick = onListItemClick;
        this.arquivos = arquivos;
    }

    @NonNull
    @Override
    public RetrieveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_retrive, parent, false);
        return new RetrieveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetrieveViewHolder holder, int position) {
        holder.textViewRetrive.setText(arquivos.get(position).getName());

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewRetrive.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
    }

    @Override
    public int getItemCount() {
        return arquivos.size();
    }

    public class RetrieveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewRetrive;
        private final CardView cardViewUserView;

        public RetrieveViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRetrive = itemView.findViewById(R.id.textViewRetrive);
            cardViewUserView = itemView.findViewById(R.id.userView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(arquivos.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(arquivos.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Arquivo arquivo, int posicao);
    }
}
