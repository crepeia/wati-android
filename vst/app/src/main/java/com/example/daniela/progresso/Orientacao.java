package com.example.daniela.progresso;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DicasDAO;
import com.example.daniela.progresso.Entidade.Dicas;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Orientacao extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private DicasDAO dicasDAO;
    private Dicas dicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);

        dicas = new Dicas();
        dbsqLite = new DBSQLite(Orientacao.this);

        try {
            dicasDAO = new DicasDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] orientacoes = new String[] {"dica 01",
                "dica 02",
                "dica 03",
                "dica 04",
                "dica 05",
                "dica 06",
                "dica 07",
                "dica 08",
                "dica 09",
                "dica 10"};

        if(dicasDAO != null){
            System.out.println("NÂO está vazio o DAO de dicas no APP, não pode criar dicas");
        }else{
            for(int i = 0; i < orientacoes.length; i++){
                dicas.setDescricao(orientacoes[i]);
                dicas.setGostou(false);
                dicas.setLida(false);
                try {
                    dicasDAO.create(dicas);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        exibePopupDica(orientacoes, dicas);


    }

    public void exibePopupDica(final String dica[], final Dicas dicas){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

      /*  Random gerador = new Random();

        //imprime sequência de 10 números inteiros aleatórios entre 0 e 10
        for (int i = 0; i < 1; i++) {
            System.out.println(gerador.nextInt(11));
        }*/

        alertDialog.setTitle(dica[0]);
        alertDialog.setMessage("Informe abaixo se gostou ou não da dica");

        alertDialog.setPositiveButton("Gostei",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dicas.setGostou(true);
                        dicas.setLida(true);
                        dicas.setUser(UserManager.getUser());
                        try {
                            dicasDAO.update(dicas);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialog.setNegativeButton("Não Gostei",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dicas.setGostou(false);
                        dicas.setLida(true);
                        dicas.setUser(UserManager.getUser());
                        try{
                            dicasDAO.update(dicas);
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                });

        alertDialog.show();
    }

}
