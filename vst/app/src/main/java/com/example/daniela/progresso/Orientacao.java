package com.example.daniela.progresso;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daniela.progresso.DAO.AcaoDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;
import com.example.daniela.progresso.DAO.DicasDAO;
import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.Dicas;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Orientacao extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private DicasDAO dicasDAO;
    private AcaoDAO acaoDAO;
    private DesafioDAO desafioDAO;
    Dicas dicas;
    Acao acao;
    Desafio desafio;

    String[] orientacoes;
    TextView dica;
    Button gostei;
    Button naoGostei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);

        dica = (TextView) findViewById(R.id.dica);
        gostei = (Button) findViewById(R.id.gostei);
        naoGostei = (Button) findViewById(R.id.naoGostei);

        dicas = new Dicas();
        acao = new Acao();
        dbsqLite = new DBSQLite(Orientacao.this);

        try {
            dicasDAO = new DicasDAO(dbsqLite.getConnectionSource());
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        orientacoes = new String[]{"A fissura dura em média de 2 a 5 minutos. Nesses momentos beba um copo de água ou escove os dentes.",
                "Escovar os dentes após comer ou beber algo que você associa ao cigarro pode diminuir sua vontade de fumar.",
                "Quando sentir vontade de acender um cigarro coma alimentos de baixo de teor calórico como por exemplo, frutas, cenouras, salsão, pepino, etc.",
                "Durante o processo de cessação é comum ocorrer episódios de recaída, mas não desanime, continue firme no seu propósito de parar de fumar.",
                "Comer frutas ou alimentos de baixas calorias ajudam nos momentos de fissura e a não ganhar peso.",
                "Parar de fumar aumenta a expectativa e a qualidade de vida.",
                "Tenha sempre perto de você garrafas de água e quando tiver vontade de fumar tome alguns goles da água.",
                "Ao parar de fumar você se sentirá melhor fisicamente e terá mais disposição para realizar exercícios físicos.",
                "Ao deixar de fumar você reduz as chances de desenvolver doenças do coração, doenças pulmonares e cânceres.",
                "Se além de você existe outro fumante em casa, peça para que ele evite fumar perto de você e proponha a essa pessoa uma tentativa de parar. Um poderá dar suporte ao outro.",
                "Guarde o dinheiro que você gasta com cigarros e ao final de um ano você poderá investir algo prazeroso, como uma viagem, por exemplo.",
                "Evite comidas ou bebidas que você associa ao cigarro, como por exemplo, café, álcool, doces, etc.",
                "Pense nas suas atividades diárias e em que momento você tem vontade de fumar e procure mudar esse comportamento.",
                "Participar de grupos de apoio para fumantes/ex-fumantes podem aumentar suas chances de parar de fumar.",
                "Em 15 anos após parar,  os riscos de sofrer de doenças cardiacas são iguais a de uma pessoa que nunca fumou.",
                "Não desista diante das recaídas. A maioria das pessoas que conseguem deixar de fumar não conseguem da primeira vez.",
                "Comunique seus familiares e amigos próximos sobre seu processo de parar de fumar e peça a compreensão deles durante esse período.",
                "Identifique as situações que te levam a fumar e procure evitá-las.",
                "Ao invés de acender um cigarro quando você estiver estressado, respire profundamente durante alguns minutos e tente relaxar.",
                "Procure o apoio de sua família e amigos se você estiver precisando desabafar.",
                "Evite frequentar ambientes onde todos fumam para não despertar o desejo de acender um cigarro.",
                "A prática de exercícios físicos pode ajudar a diminuir a ansiedade e evitar o ganho de peso.",
                "Procure evita situações estressantes durante o processo de parar de fumar.",
                "Beba bastante água durante o dia para ajudar na desintoxicação da nicotina.",
                "Tente mudar sua rotina para evitar situações associadas ao hábito de fumar.",
                "Faça uma lista de razões pelas quais é importante você parar de fumar.",
                "Procure o apoio de grupos de pessoas que também estão tentando parar de fumar. Há grupos de ajuda na internet, inclusive Facebook",
                "Evite comportamentos ou situações que reforcem o desejo de acender um cigarro.",
        };

        /*for(int i = 0; i < orientacoes.length; i++) {
            dicas.setDescricao(orientacoes[i]);
            dicas.setGostou(false);
            dicas.setLida(false);
            try {
                dicasDAO.create(dicas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/

        this.pontoDica();

        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_MONTH);

        dica.setText(orientacoes[i]);

        dicas.setDescricao(orientacoes[i]);
        dicas.setGostou(true);
        dicas.setLida(true);
        dicas.setDate(calendar.getTime());
        dicas.setUser(UserManager.getUser());

        try {
            System.out.println("TRY 1 ");
            dicasDAO.createOrUpdate(dicas);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("DESCRIÇÃO : " + dicas.getDescricao());
        System.out.println("GOSTOU: " + dicas.isGostou());
        System.out.println("LIDA : " + dicas.isLida());
        System.out.println("DATE : " + dicas.getDate());
        System.out.println("USER: " + dicas.getUser().getName());

    }

    public void ButtonGostei(View v) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        dicas.setDescricao(orientacoes[i]);
        dicas.setLida(true);
        dicas.setDate(calendar.getTime());
        dicas.setUser(UserManager.getUser());

        try {
            System.out.println("TRY 1 ");
            dicasDAO.createOrUpdate(dicas);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.pontoDica();

        System.out.println("DESCRIÇÃO : " + dicas.getDescricao());
        System.out.println("GOSTOU: " + dicas.isGostou());
        System.out.println("LIDA : " + dicas.isLida());
        System.out.println("DATE : " + dicas.getDate());
        System.out.println("USER: " + dicas.getUser().getName());

    }

    public void ButtonNaoGostei(View v) {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        dicas.setDescricao(orientacoes[i]);
        dicas.setGostou(false);
        dicas.setLida(true);
        dicas.setDate(calendar.getTime());
        dicas.setUser(UserManager.getUser());

        try {
            System.out.println("TRY 2 ");
            dicasDAO.createOrUpdate(dicas);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.pontoDica();
        System.out.println("DESCRIÇÃO : " + dicas.getDescricao());
        System.out.println("GOSTOU: " + dicas.isGostou());
        System.out.println("LIDA : " + dicas.isLida());
        System.out.println("DATE : " + dicas.getDate());
        System.out.println("USER: " + dicas.getUser().getName());

    }

    public void exibePopupDica(final Dicas dicas) {

        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_MONTH);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(orientacoes[i]);
        alertDialog.setMessage("Informe abaixo se gostou ou não da dica");

        alertDialog.setPositiveButton("Gostei",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        int i = calendar.get(Calendar.DAY_OF_MONTH);
                        dicas.setDescricao(orientacoes[i]);
                        dicas.setGostou(true);
                        dicas.setLida(true);
                        dicas.setUser(UserManager.getUser());

                        System.out.println("DESCRIÇÃO : " + dicas.getDescricao());
                        System.out.println("GOSTOU: " + dicas.isGostou());
                        System.out.println("LIDA : " + dicas.isLida());
                        System.out.println("USER: " + dicas.getUser().getName());
                        // try {
                        //dicasDAO.createOrUpdate(dicas);
                        //} catch (SQLException e) {
                        //   e.printStackTrace();
                        //}
                    }
                });

        alertDialog.setNegativeButton("Não Gostei",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        int i = calendar.get(Calendar.DAY_OF_MONTH);
                        dicas.setDescricao(orientacoes[i]);
                        dicas.setGostou(false);
                        dicas.setLida(true);
                        dicas.setUser(UserManager.getUser());

                        System.out.println("DESCRIÇÃO : " + dicas.getDescricao());
                        System.out.println("GOSTOU: " + dicas.isGostou());
                        System.out.println("LIDA : " + dicas.isLida());
                        System.out.println("USER: " + dicas.getUser().getName());
                        // dicasDAO.createOrUpdate(dicas);
                    }
                });

        alertDialog.show();
    }

    public void pontoDica() {
        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visualizar dicas diariamente");
            for (Desafios d : desafiosList) {
                System.out.println(d.getTitulo());
                System.out.println(d.getDescricao());
                System.out.println(d.getPontuacao());
                System.out.println(d.getTipo());
                System.out.println(d.getVariacao());

                acao.setPonto(d.getPontuacao());
                acao.setUser(UserManager.getUser());
                acao.setData(Calendar.getInstance().getTime());
                acao.setDesafio(desafiosList.get(0));

                acaoDAO.createOrUpdate(acao);

                System.out.println("TESTE" + acao.getData());
                System.out.println("TESTE" + acao.getDesafio().getId());
                System.out.println("TESTE" + acao.getUser().getName());
                System.out.println("TESTE" + acao.getPonto());
                System.out.println("TESTE" + acao.getId());

               //
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
