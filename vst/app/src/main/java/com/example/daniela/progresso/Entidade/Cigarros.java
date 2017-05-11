package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by daniela on 02/05/17.
 */
@DatabaseTable (tableName = "cigarros")
public class Cigarros implements Serializable {

    @DatabaseField(columnName = "id_cigarro",  generatedId = true)
    private long id;

    @DatabaseField(columnName = "data")
    private Date date;

    @DatabaseField(columnName = "salvo")
    private boolean salvo;

    @DatabaseField(columnName = "cigarros_diario")
    private int cigarrosDiario;

    @DatabaseField(canBeNull = false, foreign = true) //foreignAutoRefresh = true)
    public User user;

    public Cigarros(){
    }

    public Cigarros(Date date, boolean salvo, int cigarrosDiario, User user) {
        this.date = date;
        this.salvo = salvo;
        this.cigarrosDiario = cigarrosDiario;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCigarrosDiario() {
        return cigarrosDiario;
    }

    public void setCigarrosDiario(int cigarrosDiario) {
        this.cigarrosDiario = cigarrosDiario;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSalvo() {
        return salvo;
    }

    public void setSalvo(boolean salvo) {
        this.salvo = salvo;
    }
}
