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

    @DatabaseField(columnName = "ponto_dica")
    private int pontoDica;

    @DatabaseField(columnName = "ponto_site")
    private int pontoSite;

    @DatabaseField(columnName = "ponto_cadastroApp")
    private int pontoCadastroApp;

    @DatabaseField(columnName = "ponto_registro")
    private int pontoRegistro;

    @DatabaseField(columnName = "ponto_naoFumar")
    private int pontoNaoFumar;

    public Pontos(){}

    public Pontos(User user, int pontoDica, int pontoSite, int pontoCadastroApp, int pontoRegistro, int pontoNaoFumar){
        this.user = user;
        this.pontoDica = pontoDica;
        this.pontoSite = pontoSite;
        this.pontoCadastroApp = pontoCadastroApp;
        this.pontoRegistro = pontoRegistro;
        this.pontoNaoFumar = pontoNaoFumar;

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

    public int getPontoDica() {
        return pontoDica;
    }

    public void setPontoDica(int pontoDica) {
        this.pontoDica = pontoDica;
    }

    public int getPontoSite() {
        return pontoSite;
    }

    public void setPontoSite(int pontoSite) {
        this.pontoSite = pontoSite;
    }

    public int getPontoCadastroApp() {
        return pontoCadastroApp;
    }

    public void setPontoCadastroApp(int pontoCadastroApp) {
        this.pontoCadastroApp = pontoCadastroApp;
    }

    public int getPontoRegistro() {
        return pontoRegistro;
    }

    public void setPontoRegistro(int pontoRegistro) {
        this.pontoRegistro = pontoRegistro;
    }

    public int getPontoNaoFumar() {
        return pontoNaoFumar;
    }

    public void setPontoNaoFumar(int pontoNaoFumar) {
        this.pontoNaoFumar = pontoNaoFumar;
    }
}
