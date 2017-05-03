package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by daniela on 02/05/17.
 */
@DatabaseTable (tableName = "cigarros")
public class Cigarros{

    @DatabaseField(columnName = "data")
    private Date date;

    @DatabaseField(columnName = "cigarros")
    private int cigarros;

    @DatabaseField(columnName = "valor_maco")
    private float valorMaco;

    @DatabaseField(columnName = "cigarros_diario")
    private int cigarrosDiario;

    @DatabaseField(columnName = "dinheiro_economizado")
    private float dinheiroEconomizado;

    @DatabaseField(columnName = "dinheiro_nao_economizado")
    private float dinheiroNaoEconomizado;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public User user;

    public Cigarros(){
    }

    public Cigarros(Date date, int cigarros, float valorMaco, int cigarrosDiario, float dinheiroEconomizado, float dinheiroNaoEconomizado, User user) {
        this.date = date;
        this.cigarros = cigarros;
        this.valorMaco = valorMaco;
        this.cigarrosDiario = cigarrosDiario;
        this.dinheiroEconomizado = dinheiroEconomizado;
        this.dinheiroNaoEconomizado = dinheiroNaoEconomizado;
        this.user = user;
    }







}
