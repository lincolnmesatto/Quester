package com.pucpr.quester.model;

public class UsuarioAlunoModel {
    private Aluno aluno;
    private Usuario usuario;

    public UsuarioAlunoModel() {
    }

    public UsuarioAlunoModel(Aluno aluno, Usuario usuario) {
        this.aluno = aluno;
        this.usuario = usuario;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
