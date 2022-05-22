package com.pucpr.quester.model;

import java.util.List;

public class InstiuicaoDependentes {
    private String id;
    private List<Turma> turmas;
    private List<Aluno> alunos;
    private List<Usuario> professores;
    private List<Recompensa> recompensas;

    public InstiuicaoDependentes() {
    }

    public InstiuicaoDependentes(String id, List<Turma> turmas, List<Aluno> alunos, List<Usuario> professores, List<Recompensa> recompensas) {
        this.id = id;
        this.turmas = turmas;
        this.alunos = alunos;
        this.professores = professores;
        this.recompensas = recompensas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Usuario> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Usuario> professores) {
        this.professores = professores;
    }

    public List<Recompensa> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(List<Recompensa> recompensas) {
        this.recompensas = recompensas;
    }
}
