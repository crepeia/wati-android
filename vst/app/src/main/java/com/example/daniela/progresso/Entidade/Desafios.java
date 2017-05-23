package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by daniela on 02/05/17.
 */
@DatabaseTable (tableName = "desafio")
public class Desafios implements Serializable{

    @DatabaseField(columnName = "id_desafios", generatedId = true)
    private long id;

    @DatabaseField(columnName = "descricao")
    private String descricao;

    @DatabaseField(columnName = "tipo")
    private int tipo;

    @DatabaseField(columnName = "pontuacao")
    private int pontuacao;

    @DatabaseField(columnName = "variacao")
    private int variacao;

    public Desafios(){
    }

    public Desafios(String descricao, int tipo, int pontuacao, int variacao){
        this.descricao = descricao;
        this.tipo = tipo;
        this.pontuacao = pontuacao;
        this.variacao = variacao;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getVariacao() {
        return variacao;
    }

    public void setVariacao(int variacao) {
        this.variacao = variacao;
    }

}

