package com.pucpr.quester.model;

public class Classe {

    private String id;
    private String nome;
    private int bonus;
    private String idDisciplina;

    public Classe(String id, String nome, int bonus, String idDisciplina) {
        this.id = id;
        this.nome = nome;
        this.bonus = bonus;
        this.idDisciplina = idDisciplina;
    }

    public Classe() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

}
