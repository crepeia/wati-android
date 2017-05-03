package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by daniela on 02/05/17.
 */
@DatabaseTable (tableName = "pontuacao")
public class Pontuacao {
    //@DatabaseField(columnName = "id", generatedId = true)
    //private int id;

    @DatabaseField(columnName = "data_acao")
    private Date data;
    @DatabaseField(columnName = "ponto")
    private int ponto;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public User user;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public Acoes acao;

    public Pontuacao(){
    }

    //For our own purpose, so it's easier to create a StudentDetails object
    public Pontuacao(Date data, int ponto, User user, Acoes acao){
        this.data = data;
        this.ponto = ponto;
        this.user = user;
        this.acao = acao;

    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getPonto() {
        return ponto;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Acoes getAcao() {
        return acao;
    }

    public void setAcao(Acoes acao) {
        this.acao = acao;
    }
}

