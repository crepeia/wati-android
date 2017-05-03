package com.example.daniela.progresso.Entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by daniela on 26/04/17.
 */

@DatabaseTable
public class Acoes {

    @DatabaseField(allowGeneratedIdInsert=true, generatedId=true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true)
    private User user;








}
