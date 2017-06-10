package com.example.daniela.progresso.DAO;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniela.progresso.Entidade.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by daniela on 25/04/17.
 */

public class UserDAO extends BaseDaoImpl<User, Integer>{
    public UserDAO(ConnectionSource cs) throws SQLException {
        super(User.class);
        setConnectionSource(cs);
        initialize();
    }

}