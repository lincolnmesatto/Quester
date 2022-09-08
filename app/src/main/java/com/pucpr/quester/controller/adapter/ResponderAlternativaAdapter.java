package com.pucpr.quester.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.DataModelResposta;

import java.util.List;

import androidx.annotation.NonNull;
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
        holder.tvAlternativaResposta.setText(DataModelResposta.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).getAlternativa());

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

        private final TextView tvAlternativaResposta;
        private final CheckBox checkBoxQuestaoResposta;

        public ResponderAlternativaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlternativaResposta = itemView.findViewById(R.id.tvAlternativaResposta);
            checkBoxQuestaoResposta = itemView.findViewById(R.id.checkBoxQuestaoResposta);

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
