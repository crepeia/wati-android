package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by daniela on 20/05/17.
 */
@DatabaseTable (tableName = "dicas")
public class Dicas implements Serializable {

    @DatabaseField(columnName = "id_dicas", generatedId = true)
    private long id;

    @DatabaseField(columnName = "descricao_dica")
    private String descricao;

    @DatabaseField(columnName = "lida")
    private boolean lida;

    @DatabaseField(columnName = "gostou")
    private boolean gostou;

    @DatabaseField(canBeNull = false, foreign = true) //foreignAutoRefresh = true)
    public User user;

    public Dicas(){ }

    public Dicas(String descricao, boolean lida,boolean gostou, User user){
        this.descricao = descricao;
        this.lida = lida;
        this.gostou = gostou;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public boolean isGostou() { return gostou; }

    public void setGostou(boolean gostou) { this.gostou = gostou; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
