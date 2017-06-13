package com.example.daniela.progresso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.daniela.progresso.DAO.AcaoDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;
import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Desafios;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class Sobre extends AppCompatActivity {

    private Acao acao;
    private Desafios desafios;
    private AcaoDAO acaoDAO;
    private DesafioDAO desafioDAO;
    private DBSQLite dbsqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        acao = new Acao();
        desafios = new Desafios();
        dbsqLite = new DBSQLite(Sobre.this);
        try {
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void linkSite(View view){
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vivasemtabaco.com.br"));
        //startActivity(browserIntent);

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visitar o site do Viva Sem Tabaco");
            for (Desafios d : desafiosList){
                System.out.println(d.getTitulo());
                System.out.println(d.getDescricao());
                System.out.println(d.getPontuacao());
                System.out.println(d.getTipo());
                System.out.println(d.getVariacao());

                //if(d.getTipo() == 2) { //não é contínua a pontuação! Se não é contínua então não tem variação.
                    acao.setPonto(d.getPontuacao());
                    acao.setUser(UserManager.getUser());
                    acao.setData(Calendar.getInstance().getTime());
                    acao.setDesafio(desafiosList.get(0));
                    acaoDAO.create(acao);
            }//else{      É CONTINUA, ENTAO CALCULAR OS PONTOS PARA CONTINUA.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
