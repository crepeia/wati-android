package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by daniela on 26/04/17.
 */

@DatabaseTable(tableName = "acao")
public class Acao implements Serializable {

    @DatabaseField(columnName = "id_acao", generatedId=true)
    private long id;

    @DatabaseField(columnName = "ponto")
    private int ponto;

    @DatabaseField(columnName = "data")
    private Date data;

    @DatabaseField(canBeNull = false, foreign = true) //foreignAutoRefresh = true)
    public User user;

    @DatabaseField(canBeNull = false, foreign = true)
    public Desafios desafio;

    public Acao(){}

    public Acao(int ponto, Date data, User user, Desafios desafio){
        this.ponto = ponto;
        this.data = data;
        this.user = user;
        this.desafio = desafio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPonto() {
        return ponto;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Desafios getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafios desafio) {
        this.desafio = desafio;
    }
}
