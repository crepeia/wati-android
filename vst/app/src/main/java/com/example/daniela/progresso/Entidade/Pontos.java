package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by daniela on 12/06/17.
 */
@DatabaseTable(tableName = "pontos")
public class Pontos implements Serializable {

    @DatabaseField(columnName = "id_pontos", generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false, foreign = true) //foreignAutoRefresh = true)
    public User user;

    @DatabaseField(columnName = "lida")
    private int ponto;

    public Pontos(){}

    public Pontos(User user, int ponto){
        this.user = user;
        this.ponto = ponto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPonto() {
        return ponto;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }
}
