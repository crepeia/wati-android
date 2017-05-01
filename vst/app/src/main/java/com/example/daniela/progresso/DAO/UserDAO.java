package com.example.daniela.progresso.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniela.progresso.Entidade.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.BaseDaoEnabled;
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
/*public class UserDAO {

    Context context;
    DBSQLite dbsqLite;

    public UserDAO(Context context) {
        this.context = context;
        dbsqLite = new DBSQLite(context);
    }

    public long insert(User user) throws Exception {
        try{
            SQLiteDatabase db = dbsqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(User.campo_id, user.id);
            values.put(User.campo_name, user.name);
            values.put(User.campo_email, user.email);
            values.put(User.campo_cigarros, user.cigarros);
            values.put(User.campo_valorMaco, user.valorMaco);

            Long id = db.insert(User.tabela, null, values); //null significa que não quero nenhuma columa com valor null, caso queira tem que especificar
            db.close();
            return id;

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

    public boolean alter(User user) throws Exception {

        try{
            SQLiteDatabase db = dbsqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(User.campo_id, user.id);
            values.put(User.campo_name, user.name);
            values.put(User.campo_email, user.email);
            values.put(User.campo_cigarros, user.cigarros);
            values.put(User.campo_valorMaco, user.valorMaco);

            String where = User.campo_id + " = ?";
            int id = db.update(User.tabela, values, where, new String[]{String.valueOf(user.id)}); //null significa que não quero nenhuma columa com valor null, caso queira tem que especificar
            db.close();

            return id > 0 ? true : false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public boolean delete(User user) throws Exception {


        return delete(user.id);
    }

    public boolean delete(Long id)throws Exception {

        try{
            SQLiteDatabase db = dbsqLite.getWritableDatabase();
            String where = User.campo_id + " = ?";
            int ret = db.delete(User.tabela, where, new String[]{String.valueOf(id)});
            return ret > 0 ? true : false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> select() throws Exception {

        ArrayList<User> lista = new ArrayList<>();
        try{
            SQLiteDatabase db = dbsqLite.getWritableDatabase();

            String selectQuery = "SELECT " +
                    User.campo_id + "," +
                    User.campo_name + "," +
                    User.campo_email + "," +
                    User.campo_cigarros + "," +
                    User.campo_valorMaco +
                    " FROM " + User.tabela;

            Cursor cursor = db.rawQuery(selectQuery, null);

            User usr;

            if (cursor.moveToFirst()) {
                do {
                    usr = new User();
                    usr.id = cursor.getLong(0);
                    usr.name = cursor.getString(1);
                    usr.email = cursor.getString(2);
                    usr.cigarros = cursor.getInt(3);
                    usr.valorMaco = cursor.getInt(4);
                    lista.add(usr);

                } while (cursor.moveToNext());
                db.close();
            }

            //return lista;

        }catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }*/
