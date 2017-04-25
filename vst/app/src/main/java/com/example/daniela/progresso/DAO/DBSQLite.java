package com.example.daniela.progresso.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.daniela.progresso.Entidade.User;

/**
 * Created by daniela on 25/04/17.
 */

public class DBSQLite extends SQLiteOpenHelper {

    private static final String NAME_BASE = "base.db";
    private static final int VERSAO =   1;

    public DBSQLite(Context context) {
        super(context, NAME_BASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_USER =
                "CREATE TABLE " + User.tabela + "("
                + User.campo_id + "INTEGER PRIMARY KEWY AUTOINCREMENT , "
                + User.campo_name + "VARCHAR, "
                + User.campo_email + "VARCHAR ) ";
        db.execSQL(CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
