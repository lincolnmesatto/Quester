package com.pucpr.quester.controller;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pucpr.quester.R;
import com.pucpr.quester.model.Alternativa;
import com.pucpr.quester.model.DataModel;
import com.pucpr.quester.model.Questao;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlternativaAdapter extends RecyclerView.Adapter<AlternativaAdapter.AlternativaViewHolder> {

    private final OnListItemClick onListItemClick;

    List<Alternativa> alternativas;
    int posicaoQuestao;

    public AlternativaAdapter(OnListItemClick onListItemClick, List<Alternativa> alternativas, int posicaoQuestao) {
        this.onListItemClick = onListItemClick;
        this.alternativas = alternativas;
        this.posicaoQuestao = posicaoQuestao;
    }

    @NonNull
    @Override
    public AlternativaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_alternativas, parent, false);
        return new AlternativaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativaViewHolder holder, int position) {
        //holder.editTextAlternativas.setText(alternativas.get(position).getAlternativa());

        holder.editTextAlternativas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alternativas.get(holder.getAdapterPosition()).setAlternativa(holder.editTextAlternativas.getText().toString());
                if(DataModel.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas() == null)
                    DataModel.getInstance().getQuestoesDataModel().get(posicaoQuestao).setAlternativas(new ArrayList<>());
                DataModel.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).setAlternativa(holder.editTextAlternativas.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.checkBoxQuestao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                alternativas.get(holder.getAdapterPosition()).setCorreta(isChecked ? 1 : 0);
                DataModel.getInstance().getQuestoesDataModel().get(posicaoQuestao).getAlternativas().get(holder.getAdapterPosition()).setCorreta(isChecked ? 1 : 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alternativas.size();
    }

    public class AlternativaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView editTextAlternativas;
        private final CheckBox checkBoxQuestao;
//        private final TextView textViewCpfAluno;

        public AlternativaViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextAlternativas = itemView.findViewById(R.id.editTextAlternativas);
            checkBoxQuestao = itemView.findViewById(R.id.checkBoxQuestao);
//            textViewCpfAluno = itemView.findViewById(R.id.textViewCpfAluno);

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
