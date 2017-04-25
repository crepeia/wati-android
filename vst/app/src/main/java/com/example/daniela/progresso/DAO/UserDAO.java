package com.example.daniela.progresso.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniela.progresso.Entidade.User;

import java.util.ArrayList;

/**
 * Created by daniela on 25/04/17.
 */

public class UserDAO {

    Context context;
    DBSQLite dbsqLite;

    public UserDAO(Context context) {
        this.context = context;
        dbsqLite = new DBSQLite(context);
    }

    public long insert(User user) {

        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.campo_id, user.id);
        values.put(User.campo_name, user.name);
        values.put(User.campo_email, user.email);

        Long id = db.insert(User.tabela, null, values); //null significa que não quero nenhuma columa com valor null, caso queira tem que especificar
        db.close();

        return id;
    }

    public boolean alter(User user) {

        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.campo_id, user.id);
        values.put(User.campo_name, user.name);
        values.put(User.campo_email, user.email);

        String where = User.campo_id + " = ?";
        int id = db.update(User.tabela, values, where, new String[]{String.valueOf(user.id)}); //null significa que não quero nenhuma columa com valor null, caso queira tem que especificar
        db.close();

        return id > 0 ? true : false;

    }

    public boolean delete(User user) {


        return delete(user.id);
    }

    public boolean delete(Long id) {

        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        String where = User.campo_id + " = ?";
        int ret = db.delete(User.tabela, where, new String[]{String.valueOf(id)});
        return ret > 0 ? true : false;
    }

    public ArrayList<User> select() {

        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ArrayList<User> lista = new ArrayList<>();

        String selectQuery = "SELECT " +
                User.campo_id + "," +
                User.campo_name + "," +
                User.campo_email + "," +
                " FROM " + User.tabela;

        Cursor cursor = db.rawQuery(selectQuery, null);

        User usr;

        if (cursor.moveToFirst()) {
            do {
                usr = new User();
                usr.id = cursor.getLong(0);
                usr.name = cursor.getString(1);
                usr.email = cursor.getString(2);
                lista.add(usr);

            } while (cursor.moveToNext());
            db.close();
        }

        return lista;

    }
}
