package com.example.daniela.progresso;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.daniela.progresso.DAO.AcaoDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;

import com.example.daniela.progresso.Entidade.Desafios;

import java.sql.SQLException;
import java.util.ArrayList;

public class Desafio extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private DesafioDAO desafioDAO;
    Desafios desafios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        dbsqLite = new DBSQLite(Desafio.this);
        desafios = new Desafios();

        try {
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listview = (ListView) findViewById(R.id.listDesafios);
        String[] values = new String[] { "Visitar site",
                "Ver dicas",
                "Ficar dias sem fumar",
                "Preencher plano para parar",
                "Fazer cadastro no app",
                "Completar cadastro",
                "Completar Fargestron",
                "Informar o registro diário"};

        final ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);


        listview.setAdapter(adapter);


    }

    public void carregaDescricaoBD(ArrayList<String> arrayList) throws SQLException {
        int i;
        for (i = 0; i < arrayList.size(); i++){
            desafios.setDescricao(arrayList.get(i));
            desafioDAO.create(desafios);
        }
    }


}
