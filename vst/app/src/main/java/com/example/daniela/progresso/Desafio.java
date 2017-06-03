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

        desafios = new Desafios();
        dbsqLite = new DBSQLite(Desafio.this);
        try {
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            salvaDesafios();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvaDesafios() throws SQLException {
        String[] values = new String[] { "Visitar site",
                "Ver dicas",
                "Ficar dias sem fumar",
                "Preencher plano para parar",
                "Fazer cadastro no app",
                "Completar cadastro",
                "Completar Fargestron",
                "Informar o registro diário"};

        int vetorPontuação[] = {1, 2, 3, 4, 5, 6, 7, 8}; //pontos de cada desafio

        int vetorTipo[] = {1, 1, 2, 2, 1, 2, 2, 2}; //diz se a pontuação é continua ou não. 1 é contínua, 2 não é contínua

        int vetorVariacao[] = {0, 1, 1, 0, 0, 0, 0, 1}; //o incremento de cada desafio, caso seja continuo

        if(desafioDAO == null)
            System.out.println("hahahhahahah");
        else
            System.out.println("desafio: " + desafioDAO);
        /*for (int i = 0; i < values.length; i++){
            desafios.setDescricao(values[i]);
            desafios.setPontuacao(vetorPontuação[i]);
            desafios.setTipo(vetorTipo[i]);
            desafios.setVariacao(vetorVariacao[i]);

            try {
                desafioDAO.create(desafios);
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
    }


}
       /* final ListView listview = (ListView) findViewById(R.id.listDesafios);
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

        int vetorPontuação[] = {1, 2, 3, 4, 5, 6, 7, 8};

        int vetorTipo[] = {1, 1, 3, 2, 1, 3, 2, 2};

        int vetorVariacao[] = {0, 1, 1, 0, 0, 0, 0, 1};
//


        for (int j = 0; j < values.length; j++){ //fazer isso só uma vez
            desafios.setDescricao(values[j]);
            desafios.setPontuacao(vetorPontuação[j]);
            desafios.setTipo(vetorTipo[j]);
            desafios.setVariacao(vetorVariacao[j]);*/

            /*try {
                desafioDAO.create(desafios);
            } catch (SQLException e) {
                e.printStackTrace();
            }*/


//            desafios.setPontuacao(20);
//            desafios.setTipo(1);
//            desafios.setVariacao(1);

/*        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);


        listview.setAdapter(adapter);


    }

   /* public void carregaDescricaoBD(ArrayList<String> arrayList) throws SQLException {
        int i;
        for (i = 0; i < arrayList.size(); i++){
            desafios.setDescricao(arrayList.get(i));
            desafioDAO.create(desafios);
        }
    }*/


//}
