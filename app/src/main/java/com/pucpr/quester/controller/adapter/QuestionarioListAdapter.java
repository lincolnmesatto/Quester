package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.pucpr.quester.R;
import com.pucpr.quester.model.Disciplina;
import com.pucpr.quester.model.Questionario;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionarioListAdapter extends RecyclerView.Adapter<QuestionarioListAdapter.QuestionarioListViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Questionario> questionarios;
    List<Disciplina> disciplinas;

    public QuestionarioListAdapter(OnListItemClick onListItemClick, List<Questionario> questionarios, List<Disciplina> disciplinas) {
        this.onListItemClick = onListItemClick;
        this.questionarios = questionarios;
        this.disciplinas = disciplinas;
    }

    @NonNull
    @Override
    public QuestionarioListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_questionario_list, parent, false);
        return new QuestionarioListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionarioListViewHolder holder, int position) {
        holder.textViewListTitulo.setText(questionarios.get(position).getTitulo());
        for (Disciplina d : disciplinas) {
            if(questionarios.get(position).getIdDisciplina().equals(d.getId())){
                holder.textViewListDisciplina.setText(d.getNome());
                break;
            }
        }
        holder.textViewListQtdQuestao.setText("Nº de questões: "+questionarios.get(position).getQuestoes().size());
        holder.textViewListXp.setText("Experiência: "+questionarios.get(position).getXp());

        holder.cardViewUserView.setCardBackgroundColor(position % 2 == 0? Color.parseColor("#474747"):Color.parseColor("#FFFFFF"));
        holder.textViewListQtdQuestao.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewListDisciplina.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewListXp.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));
        holder.textViewListTitulo.setTextColor(position % 2 == 0?Color.parseColor("#FFFFFF"):Color.parseColor("#474747"));

    }

    @Override
    public int getItemCount() {
        return questionarios.size();
    }

    public class QuestionarioListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewListTitulo;
        private final TextView textViewListDisciplina;
        private final TextView textViewListQtdQuestao;
        private final TextView textViewListXp;
        private final CardView cardViewUserView;

        public QuestionarioListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewListTitulo = itemView.findViewById(R.id.textViewListTitulo);
            textViewListDisciplina = itemView.findViewById(R.id.textViewListDisciplina);
            textViewListQtdQuestao = itemView.findViewById(R.id.textViewListQtdQuestao);
            textViewListXp = itemView.findViewById(R.id.textViewListXp);
            cardViewUserView = itemView.findViewById(R.id.userView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClick == null)
                        return;
                    onListItemClick.onItemClick(questionarios.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(onListItemClick == null)
                        return false;

                    return onListItemClick.onItemLongClick(questionarios.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(questionarios.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Questionario questionario, int posicao);
        boolean onItemLongClick(Questionario questionario, int posicao);
    }
}
