package com.pucpr.quester.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.DataModelResposta;
import com.pucpr.quester.model.QuestaoResposta;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ResponderAlternativaAdapter extends RecyclerView.Adapter<ResponderAlternativaAdapter.ResponderAlternativaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Alternativa> alternativas;
    int posicaoQuestao;

    public ResponderAlternativaAdapter(OnListItemClick onListItemClick, List<Alternativa> alternativas, int posicaoQuestao) {
        this.onListItemClick = onListItemClick;
        this.alternativas = alternativas;
        this.posicaoQuestao = posicaoQuestao;
    }

    @NonNull
    @Override
    public ResponderAlternativaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_alternativas_resposta, parent, false);
        return new ResponderAlternativaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponderAlternativaViewHolder holder, int position) {
        if(DataModelResposta.getInstance().isRespondido()){
            int correta = (DataModelResposta.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).getCorreta());
            int escolhida = (DataModelResposta.getInstance().getQuestoesRespostaDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).getCorreta());

            if(correta != escolhida){
                if(escolhida == 1){
                    holder.checkBoxQuestaoResposta.setChecked(true);
                    holder.cardViewAlternativaResposta.setCardBackgroundColor(Color.parseColor("#D92917"));
                }

                if(correta == 1){
                    holder.checkBoxQuestaoResposta.setChecked(true);
                    holder.cardViewAlternativaResposta.setCardBackgroundColor(Color.parseColor("#6EFA85"));
                }
            }else{
                if(correta == 1){
                    holder.checkBoxQuestaoResposta.setChecked(true);
                    holder.cardViewAlternativaResposta.setCardBackgroundColor(Color.parseColor("#6EFA85"));
                }
            }

            holder.checkBoxQuestaoResposta.setEnabled(false);
        }
        holder.checkBoxQuestaoResposta.setText(DataModelResposta.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).getAlternativa());

        holder.checkBoxQuestaoResposta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                alternativas.get(holder.getAdapterPosition()).setCorreta(isChecked ? 1 : 0);
                DataModelResposta.getInstance().getQuestoesRespostaDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).setCorreta(isChecked ? 1 : 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alternativas.size();
    }

    public class ResponderAlternativaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox checkBoxQuestaoResposta;
        private final CardView cardViewAlternativaResposta;

        public ResponderAlternativaViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxQuestaoResposta = itemView.findViewById(R.id.checkBoxQuestaoResposta);
            cardViewAlternativaResposta = itemView.findViewById(R.id.cardViewAlternativaResposta);

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
