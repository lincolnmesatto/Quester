package com.pucpr.quester.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Instituicao;
import com.pucpr.quester.model.InstituicaoDataModel;

public class InstituicaoAdapter extends RecyclerView.Adapter<InstituicaoAdapter.ViewHolder> {
    private static ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        InstituicaoAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int id, View view);
    }

    @NonNull
    @Override
    public InstituicaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.recyclerview_instituicao,
                        parent, false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstituicaoAdapter.ViewHolder holder, int position) {
        Instituicao i = InstituicaoDataModel.getInstance().getInstituicoes().get(position);
        holder.textViewName.setText(i.getNome());
    }

    @Override
    public int getItemCount() {
        return InstituicaoDataModel.getInstance().getInstituicoes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }
}
