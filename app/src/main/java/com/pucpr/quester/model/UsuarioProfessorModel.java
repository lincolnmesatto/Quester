package com.pucpr.quester.model;

public class UsuarioProfessorModel {
    private Professor professor;
    private Usuario usuario;

    public UsuarioProfessorModel() {
    }

    public UsuarioProfessorModel(Professor professor, Usuario usuario) {
        this.professor = professor;
        this.usuario = usuario;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
