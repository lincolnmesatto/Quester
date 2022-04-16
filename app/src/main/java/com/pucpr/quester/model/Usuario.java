package com.pucpr.quester.model;

public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String dtNascimento;
    private String genero;
    private String telefone;
    private String cpf;
    private int perfil;

    public Usuario() {
    }

    public Usuario(String idUsuario, String nome, String email, String dtNascimento, String genero, String telefone, String cpf, int perfil) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.dtNascimento = dtNascimento;
        this.genero = genero;
        this.telefone = telefone;
        this.cpf = cpf;
        this.perfil = perfil;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }
}
