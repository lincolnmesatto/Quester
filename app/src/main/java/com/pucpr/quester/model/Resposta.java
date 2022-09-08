package com.pucpr.quester.model;

import java.util.List;

public class Resposta {
    private String id;
    private String idQuestionario;
    private String idAluno;
    private List<QuestaoResposta> questoesResposta;

    public Resposta() {
    }

    public Resposta(String id, String idQuestionario, String idAluno, List<QuestaoResposta> questoesResposta) {
        this.id = id;
        this.idQuestionario = idQuestionario;
        this.idAluno = idAluno;
        this.questoesResposta = questoesResposta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdQuestionario() {
        return idQuestionario;
    }

    public void setIdQuestionario(String idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    public List<QuestaoResposta> getQuestoesResposta() {
        return questoesResposta;
    }

    public void setQuestoesResposta(List<QuestaoResposta> questoesResposta) {
        this.questoesResposta = questoesResposta;
    }
}
