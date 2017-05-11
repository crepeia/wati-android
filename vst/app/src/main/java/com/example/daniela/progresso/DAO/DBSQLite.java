package com.example.daniela.progresso.DAO;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Cigarros;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Created by daniela on 25/04/17.
 */

public class DBSQLite extends OrmLiteSqliteOpenHelper {

    // Campos

    public static final String DB_NAME = "watiAndroid.db";
    private static final int DB_VERSION = 3;

    private Dao<User, Integer> userDao;
    private Dao<Acao, Integer> acaoDao;
    private Dao<Cigarros, Integer> cigarrosDao;
    private Dao<Desafios, Integer> desafioDao;

    // Métodos públicos

    public DBSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }


    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Acao.class);
            TableUtils.createTable(connectionSource, Cigarros.class);
            TableUtils.createTable(connectionSource, Desafios.class);

        } catch (SQLException e) {
            Log.e(DBSQLite.class.getName(), "Unable to create datbases", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Acao.class, true);
            TableUtils.dropTable(connectionSource, Cigarros.class, true);
            TableUtils.dropTable(connectionSource, Desafios.class, true);
            onCreate(db, connectionSource);

        } catch (SQLException e) {
            Log.e(DBSQLite.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        super.close();
    }

    public Dao<User, Integer> getUserDao() throws SQLException, java.sql.SQLException {
        if (userDao == null){
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public Dao getAcaoDao() throws SQLException, java.sql.SQLException {
        if (acaoDao == null){
            acaoDao = getDao(Acao.class);
        }
        return acaoDao;
    }

    public Dao getCigarroDao() throws SQLException, java.sql.SQLException {
        if (cigarrosDao == null){
            cigarrosDao = getDao(Cigarros.class);
        }
        return cigarrosDao;
    }

    public Dao getDesafioDao() throws SQLException, java.sql.SQLException {
        if (desafioDao == null){
            desafioDao = getDao(Desafios.class);
        }
        return desafioDao;
    }
}


/*public class DBSQLite extends SQLiteOpenHelper {

    private static final String NAME_BASE = "vstGami.db";
    private static final int VERSAO =   1;

    public DBSQLite(Context context) {
        super(context, NAME_BASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_USER =
                "CREATE TABLE " + User.tabela + "("
                + User.campo_id + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + User.campo_name + " VARCHAR, "
                + User.campo_email + " VARCHAR, "
                 + User.campo_cigarros + " INTEGER, "
                + User.campo_valorMaco +  " FLOAT ) ";
        db.execSQL(CREATE_TABLE_USER);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}*/
