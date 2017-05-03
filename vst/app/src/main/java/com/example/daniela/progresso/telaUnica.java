package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.User;

import java.sql.SQLException;

public class telaUnica extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private UserDAO userDAO;
    private User user;

    EditText cigarros;
    EditText valorMaco;
    //UserDAO usrDAO;

    //private DBSQLite dbsqLite= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_unica);

        dbsqLite = new DBSQLite(telaUnica.this);
        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        user = new User();

        cigarros = (EditText) findViewById(R.id.questionCigarros);
        valorMaco = (EditText) findViewById(R.id.questionCustaMaco);
        //usrDAO = new UserDAO(this);
    }


    /*private DBSQLite getHelper(){
        if(dbsqLite == null){
            dbsqLite = OpenHelperManager.getHelper(this, DBSQLite.class);
        }
        return  dbsqLite;
    }*/

    /*protected void onDestroy() {
        super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
       /* if (dbsqLite != null) {
            OpenHelperManager.releaseHelper();
            dbsqLite = null;
        }
    }*/

    public void clicouSalvarCigarroMaco(View view) throws Exception {

        user = UserManager.getUser();
        Log.i("telaNome: ", user.getName());
        Log.i("telaID: ", String.valueOf(user.getId()));
        user.setCigarros(Integer.parseInt(cigarros.getText().toString()));
        user.setValorMaco((valorMaco.getText().toString()));


        // This is how, a reference of DAO object can be done
       // final Dao<User, Integer> userDao = getHelper().getUserDao();


        userDAO.update(user);

        if (user.getId() > 0) {
            Toast.makeText(this, "Salvo com sucesso!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Não", Toast.LENGTH_SHORT).show();
        }

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);

        dbsqLite.close();
    }

       /* User user = new User();

        user.cigarros = Integer.parseInt(cigarros.getText().toString());
        user.valorMaco = Integer.parseInt(valorMaco.getText().toString());

        user.id = usrDAO.insert(user);
        if (user.id > 0){
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Não", Toast.LENGTH_SHORT).show();
        }
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);*/
}



