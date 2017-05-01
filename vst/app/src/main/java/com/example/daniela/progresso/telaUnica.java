package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daniela.progresso.DAO.DBSQLite;
//import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class telaUnica extends AppCompatActivity {

    EditText cigarros;
    EditText valorMaco;
    //UserDAO usrDAO;

    private DBSQLite dbsqLite= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_unica);

        cigarros = (EditText) findViewById(R.id.questionCigarros);
        valorMaco = (EditText) findViewById(R.id.questionCustaMaco);
        //usrDAO = new UserDAO(this);
    }

    private DBSQLite getHelper(){
        if(dbsqLite == null){
            dbsqLite = OpenHelperManager.getHelper(this, DBSQLite.class);
        }
        return  dbsqLite;
    }

    protected void onDestroy() {
        super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
        if (dbsqLite != null) {
            OpenHelperManager.releaseHelper();
            dbsqLite = null;
        }
    }

    public void clicouSalvarCigarroMaco(View view) throws Exception {
        final User user = new User();

        user.setCigarros(Integer.parseInt(cigarros.getText().toString()));
        user.setValorMaco((valorMaco.getText().toString()));

        // This is how, a reference of DAO object can be done
        final Dao<User, Integer> userDao = getHelper().getUserDao();

        userDao.create(user);

        if (user.getId() > 0) {
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Não", Toast.LENGTH_SHORT).show();
        }

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
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



