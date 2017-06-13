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
import com.example.daniela.progresso.DAO.CigarrosDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;

import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Desafio extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private DesafioDAO desafioDAO;
    Desafios desafios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        ListView listaDesafios= (ListView) findViewById(R.id.listDesafios);


        desafios = new Desafios();
        dbsqLite = new DBSQLite(Desafio.this);
        try {
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*try {
            //salvaDesafios();
            preencheDicas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, preencheDicas());

        listaDesafios.setAdapter(adapter);


    }

    public void salvaDesafios() throws SQLException {
        String[] titulo = new String[]{"Visitar o site do Viva Sem Tabaco",
                "Visualizar dicas diariamente",
                "Não fumar",
                "Preencher no site o plano para parar ",
                "Fazer cadastro no aplicativo",
                "Completar cadastro no site",
                "Completar o teste Fagerstrom no site",
                "Informar diariamente a quantidade de cigarros fumados"};

        String[] descricao = new String[]{"Link encontrado no menu do aplicativo, na categoria Sobre.",
                "Dicas encontrado no menu do aplicativo, na categoria Dicas.",
                "Não fumar nenhum cigarro durante todo o dia.",
                "O plano para parar é um plano. ",
                "O cadastro no aplicativo já foi computado ao logar.",
                "O cadastro se encontra no site do Viva Sem Tabaco.",
                "O teste Fagerstrom é um teste de dependência à nicotina, você pode encontrá-lo no site do Viva Sem Tabaco.",
                "Diariamente você pode informar a quantidade de cigarros fumados no aplicativo. Esta função se encontra na tela principal na opção \"Cigarros fumados hoje\"."};


        int vetorPontuação[] = {5, 5, 7, 10, 10, 4, 4, 5}; //pontos de cada desafio

        int vetorTipo[] = {2, 1, 1, 2, 2, 2, 2, 1}; //diz se a pontuação é continua ou não. 1 é contínua, 2 não é contínua

        int vetorVariacao[] = {0, 2, 3, 0, 0, 0, 0, 3}; //o incremento de cada desafio, caso seja continuo

        List<Desafios> desafiosList = desafioDAO.queryForAll();
        if (desafiosList.isEmpty()) {
            System.out.println("DAO de desafios está vazio!!!");
            for (int i = 0; i < titulo.length; i++) {
                desafios.setTitulo(titulo[i]);
                desafios.setDescricao(descricao[i]);
                desafios.setPontuacao(vetorPontuação[i]);
                desafios.setTipo(vetorTipo[i]);
                desafios.setVariacao(vetorVariacao[i]);

                System.out.println(desafios.getTitulo());
                System.out.println(desafios.getDescricao());
                System.out.println(desafios.getTipo());
                System.out.println(desafios.getVariacao());

                try {
                    desafioDAO.update(desafios);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("DAO desafios diferente de vazio");
            System.out.println("desafio: " + desafioDAO);

        }
    }

    public ArrayList<String> preencheDicas(){
        try {
            List<Desafios> desafiosList = desafioDAO.queryForAll();
            ArrayList<String> desafios = new ArrayList<>();

            for(Desafios d : desafiosList) {
                desafios.add(d.getDescricao());
            }
            return desafios;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
