package com.example.daniela.progresso;

import android.content.Intent;
import android.graphics.Color;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daniela.progresso.DAO.CigarrosDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.Entidade.Cigarros;
import com.example.daniela.progresso.Entidade.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBSQLite dbsqLite;
    private CigarrosDAO cigarroDAO;
    Cigarros cigarros;
    User user;

    TextView nomeUser;
    TextView dinheiroEconomizado, dinheiroNaoEconomizado;
    TextView cigarrosFumados, cigarrosNaoFumados;
    TextView pontuacao, posicao;
    Button adicionaCigarro, subtraiCigarro;


    ArrayList<Date> dataInsercaoCigarro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataInsercaoCigarro = new ArrayList<>();

        dbsqLite = new DBSQLite(MainActivity.this);
        try {
            cigarroDAO = new CigarrosDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // cigarros = new Cigarros();
        //cigarros.setUser(UserManager.getUser());
        Calendar dateAtual = Calendar.getInstance();
        System.out.println("Data atual: " + dateAtual.getTime());

        try {//No futuro passar essas condições para dentro do DAO
            List<Cigarros> cigarrosList = cigarroDAO.queryForAll();//filtrar para usuário logado FAZER
            System.out.println("data do cigarro: " + cigarrosList.get(cigarrosList.size()-1).getDate());
            if(cigarrosList.isEmpty()) {
                System.out.println("lista vazia");
                cigarros = new Cigarros();
                cigarros.setUser(UserManager.getUser());
            }else{
                Calendar c = Calendar.getInstance();
                c.setTime(cigarrosList.get(cigarrosList.size()-1).getDate());
                System.out.println("calendario");
                if(dateAtual.get(Calendar.YEAR) == c.get(Calendar.YEAR) && dateAtual.get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR)){
                    cigarros = cigarrosList.get(cigarrosList.size() - 1);
                    System.out.println("if");
                }else{
                    System.out.println("else");
                    cigarros = new Cigarros();
                    cigarros.setUser(UserManager.getUser());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        user = UserManager.getUser();

        nomeUser = (TextView) findViewById(R.id.nomeUser);

        dinheiroEconomizado = (TextView) findViewById(R.id.economizado);
        dinheiroNaoEconomizado = (TextView) findViewById(R.id.naoEconomizado);
        cigarrosFumados = (TextView) findViewById(R.id.fumados);
        cigarrosNaoFumados = (TextView) findViewById(R.id.naoFumados);
        pontuacao = (TextView) findViewById(R.id.pontuacao);
        posicao = (TextView) findViewById(R.id.posicao);
        adicionaCigarro = (Button) findViewById(R.id.addCigarro);
        subtraiCigarro = (Button) findViewById(R.id.subCigarro);

        cigarrosFumados.setText(Integer.toString(cigarros.getCigarrosDiario()));

        Log.i("Diario:", Integer.toString(cigarros.getCigarrosDiario()));
        Log.i("Diario:", String.valueOf(cigarros.isSalvo()));


       /* SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat(" HH:mm:ss");

        String currentDateandTime = sdf.format(new Date());
        String currentDateandTime1 = sdf1.format(new Date());

        Log.i("data", currentDateandTime);
        Log.i("hora", currentDateandTime1);

        Log.i("data e hora: ", currentDateandTime + currentDateandTime1);
*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Gráfico da tela principal:

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Cigarros");

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Dias da semana");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Cigarros");
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(11);
        graph.getGridLabelRenderer().setNumHorizontalLabels(8);
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(8);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        graph.addSeries(series2);

        series.setTitle("Você");
        series.setColor(Color.RED);
        series2.setTitle("Pessoas");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i = new Intent(this, telaUnica.class);
            startActivity(i);

        } else if (id == R.id.progress) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else if (id == R.id.challenge) {
            Intent i = new Intent(this, Desafio.class);
            startActivity(i);

        } else if (id == R.id.hint) {
            Intent i = new Intent(this, Dicas.class);
            startActivity(i);

        } else if (id == R.id.about) {
            Intent i = new Intent(this, Sobre.class);
            startActivity(i);

        } else if (id == R.id.logout){
            Intent i = new Intent(this, Logout.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void dinheiroEconomizado(int cigarros){

        //ArrayList<Float> dinheiroEconomizado = new ArrayList<>();

        float valorPorCigarro = 0, dinheiroEcon = 0;

        // diferenca entre o que usuário fuma normalmente e a qtd de cigarro fumados no dia
        int cigarrosNaoFumados = (Integer.parseInt(String.valueOf(UserManager.getUser().getCigarros())) - cigarros);

        if(cigarros > UserManager.getUser().getCigarros()){ //se a qtde de cigarros fumados for maior que oq normalmente fuma
            dinheiroNãoEconomizado(cigarrosNaoFumados); //chamar esta função passando como parametro o valor alem do que normalmente fuma
        }

        valorPorCigarro = (Float.parseFloat(UserManager.getUser().getValorMaco()))/20;
        dinheiroEcon = (valorPorCigarro * cigarrosNaoFumados) + dinheiroEcon;

        //dinheiroEconomizado.add(dinheiroEcon);
    }

    public void dinheiroNãoEconomizado(int cigarros){
       // ArrayList<Float> dinheiroNaoEconomizado = new ArrayList<>();

        float valorPorCigarro = 0, dinheiroNaoEcon = 0;
        valorPorCigarro = (Float.parseFloat(UserManager.getUser().getValorMaco()))/20;

        dinheiroNaoEcon = (valorPorCigarro * cigarros) + dinheiroNaoEcon;

        //dinheiroNaoEconomizado.add(dinheiroNaoEcon);
    }

    public void addCigarro(View v) throws SQLException, ParseException {
        Calendar cal = Calendar.getInstance();

        //SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        System.out.println("data em formato DATE: " + cal.getTime());

        cigarros.setCigarrosDiario(cigarros.getCigarrosDiario() + 1);
        Log.i("cont", String.valueOf(cigarros.getCigarrosDiario()));
        cigarrosFumados.setText(Integer.toString(cigarros.getCigarrosDiario()));

        System.out.println("Valor do cigarro diario: " + cigarros.getCigarrosDiario());

        cigarros.setDate(cal.getTime());
        try {
            System.out.println("id cigarro: " + cigarros.getId());

            cigarroDAO.createOrUpdate(cigarros);
        }catch (SQLException e){
            e.printStackTrace();
        }


    }

        /*if (cigarros.isSalvo() == true) {
            cigarroDAO.update(cigarros);
            Log.i("cig", "1111111111");
            Log.i("cig", String.valueOf(cigarros.isSalvo()));
        }
        else {
            cigarros.setSalvo(true);
            cigarroDAO.create(cigarros);
            Log.i("cig", "22222222");
            Log.i("cig", String.valueOf(cigarros.isSalvo()));

        }
        cigarrosFumados.setText(Integer.toString(cigarros.getCigarrosDiario()));
        Log.i("cigarosDiarios2", String.valueOf(cigarros.getCigarrosDiario()));
        //verificaDia();
        //cigarroDAO.createOrUpdate(cigarros);
        dbsqLite.close();*/




    public void subCigarro(View v){
        if(cigarros.getCigarrosDiario() != 0)
            cigarros.setCigarrosDiario(cigarros.getCigarrosDiario() - 1);
    }

    public void onPause() {
        super.onPause();
        dbsqLite.close();
    }
        /*System.out.println("Valor do cigarro diario: " + cont);
        cigarros.setCigarrosDiario(cont);
        try {
            cigarroDAO.createOrUpdate(cigarros);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

//        if(cigarros.isSalvo() == true){
//            try {
//                cigarroDAO.update(cigarros);
//                System.out.println("111111");
//                System.out.println("Salvo é: " + String.valueOf(cigarros.isSalvo()));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }else{
//            try {
//                cigarros.setSalvo(true);
//                cigarroDAO.create(cigarros);
//                System.out.println("22222");
//                System.out.println("Salvo é: " + String.valueOf(cigarros.isSalvo()));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }


    public void pontuacao(){

    }

    public void posicao(){

    }

    public void pontosGrafico(){
    }

    public void verificaDia(){
        Calendar dataAtual = Calendar.getInstance();

        Calendar datadepoisAsMeiaNoite = Calendar.getInstance();
        datadepoisAsMeiaNoite.add(Calendar.DATE, 1);
        datadepoisAsMeiaNoite.set(Calendar.HOUR_OF_DAY, 00);
        datadepoisAsMeiaNoite.set(Calendar.MINUTE, 00);
        datadepoisAsMeiaNoite.set(Calendar.SECOND, 01);

        if(dataAtual.after(datadepoisAsMeiaNoite)){
            dbsqLite.close();
            Log.i("OBS: ", String.valueOf(dataAtual.after(datadepoisAsMeiaNoite)));

        }else{
            Log.i("OBS: ", String.valueOf(dataAtual.after(datadepoisAsMeiaNoite)));
            Log.i("OBS: ","Deu problema na verificação da data");
        }


        /*Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat ca = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String dataDepois = ca.format(c.getTime());


        Calendar cc = Calendar.getInstance();
        SimpleDateFormat ca3 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat ca2 = new SimpleDateFormat("HH:mm:ss");

        String dataAtual = ca3.format(cc.getTime());


        SimpleDateFormat ddamn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String ddaaaa = ddamn.format(datadepoisAsMeiaNoite.getTime());

        Log.i("horanova:", ddaaaa);
        Log.i("horanova:", String.valueOf(cc.after(datadepoisAsMeiaNoite)));*/


    }




}
