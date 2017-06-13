package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.User;
import com.example.daniela.progresso.ws.WSValorCigarro;

import java.sql.SQLException;

public class telaUnica extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private UserDAO userDAO;
    private User user;

    TextView nome;
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
        nome = (TextView) findViewById(R.id.bemVindo);
        nome.setText("Bem vindo " + UserManager.getUser().getName());

        Log.i("Unica ", UserManager.getUser().getName());
        Log.i("Unica: ", UserManager.getUser().getEmail());
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

        Log.i("Unica ", UserManager.getUser().getName());
        Log.i("Unica: ", UserManager.getUser().getEmail());

        Log.i("telaNome: ", user.getName());
        Log.i("telaID: ", String.valueOf(user.getId()));

        Log.i("Cigarros:", String.valueOf(user.getCigarros()));
        Log.i("ValorCigarros:", String.valueOf(user.getValorMaco()));


        user.setCigarros(Integer.parseInt(cigarros.getText().toString()));
        user.setValorMaco((valorMaco.getText().toString()));

        UserManager.setUser(user);

        userDAO.update(user);

        Log.i("ValorCigarros:", String.valueOf(user.getCigarros()));
        Log.i("ValorCigarros:", String.valueOf(user.getValorMaco()));

        //WSValorCigarro wsValorCigarro = new WSValorCigarro(Integer.parseInt(cigarros.getText().toString()), (valorMaco.getText().toString()), user.getEmail());
        //wsValorCigarro.execute();

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);

        dbsqLite.close();
    }

}



