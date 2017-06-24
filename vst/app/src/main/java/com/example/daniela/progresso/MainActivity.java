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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daniela.progresso.DAO.AcaoDAO;
import com.example.daniela.progresso.DAO.CigarrosDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;
import com.example.daniela.progresso.DAO.DicasDAO;
import com.example.daniela.progresso.DAO.PontoDAO;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Cigarros;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.Dicas;
import com.example.daniela.progresso.Entidade.Pontos;
import com.example.daniela.progresso.Entidade.User;
import com.example.daniela.progresso.ws.WSCigarro;
import com.example.daniela.progresso.ws.WSMediaCigarros;
import com.example.daniela.progresso.ws.WSNaoFumou;
import com.example.daniela.progresso.ws.WSPonto;
import com.example.daniela.progresso.ws.WSPosicao;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBSQLite dbsqLite;
    private CigarrosDAO cigarroDAO;
    private AcaoDAO acaoDAO;
    private DesafioDAO desafioDAO;
    private DicasDAO dicasDAO;
    private PontoDAO pontoDAO;
    private UserDAO userDAO;
    Cigarros cigarros;
    User user;
    Acao acao;
    Dicas dicas;
    Desafios desafio;
    Pontos pontos;

    int pontoDicas;
    int pontoFicarSemFumar;
    int pontoRegistroDiario;
    int pontoCadastroApp;

    TextView nomeUser;
    TextView dinheiroEconomizado, dinheiroNaoEconomizado;
    TextView cigarrosFumados, cigarrosNaoFumados;
    TextView pontuacao, posicao;
    Button adicionaCigarro, subtraiCigarro;


    ArrayList<Date> dataInsercaoCigarro;

    int pDica = 0, pSite = 0, pCadastroApp = 0, pRegistro = 0, pNaoFumar = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataInsercaoCigarro = new ArrayList<>();

        dbsqLite = new DBSQLite(MainActivity.this);

        System.out.println("dbsqlite: " + dbsqLite);
        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
            cigarroDAO = new CigarrosDAO(dbsqLite.getConnectionSource());
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
            dicasDAO = new DicasDAO(dbsqLite.getConnectionSource());
            pontoDAO = new PontoDAO(dbsqLite.getConnectionSource());

            System.out.println("cigarrroDAO: " + cigarroDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        acao = new Acao();
        // cigarros = new Cigarros();
        //cigarros.setUser(UserManager.getUser());
        Calendar dateAtual = Calendar.getInstance();
        System.out.println("Data atual (Formato Date): " + dateAtual.getTime());
        System.out.println("dia:" + dateAtual.get(Calendar.DAY_OF_MONTH));
        System.out.println("mes:" + dateAtual.get(Calendar.MONTH));
        System.out.println("ano:" + dateAtual.get(Calendar.YEAR));


        try {//No futuro passar essas condições para dentro do DAO
            System.out.println("entrou no try: ");

            List<Cigarros> cigarrosList = cigarroDAO.queryForAll();//filtrar para usuário logado FAZER

            System.out.println("cigarrosList" + cigarrosList);

            //System.out.println("data do cigarro: " + cigarrosList.get(cigarrosList.size()-1).getDate());

            if (cigarrosList.isEmpty()) {
                System.out.println("lista vazia");
                cigarros = new Cigarros();
                cigarros.setUser(UserManager.getUser());

                System.out.println("cigarros diario: " + cigarros.getCigarrosDiario());
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(cigarrosList.get(cigarrosList.size() - 1).getDate());
                System.out.println("calendario");
                if (dateAtual.get(Calendar.YEAR) == c.get(Calendar.YEAR) && dateAtual.get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR)) {
                    cigarros = cigarrosList.get(cigarrosList.size() - 1);
                    System.out.println("if");
                } else {
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

        try {
            pontuacao.setText(Integer.toString(this.retornaPontuacaoTotal()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Calendar dtAtual = Calendar.getInstance();
        Calendar dtAntes = Calendar.getInstance();
        //dtAntes.add(Calendar.YEAR, -1);
        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
        System.out.println("DTATUAL: " +    dtAtual);
        System.out.println("DTANTES" + dtAntes);
        System.out.println("DTATUAL: " +    dtAtual.getTime());
        System.out.println("DTANTES" + dtAntes.getTime());

        this.grafico();
        this.posicao();
        this.dinheiroEconomizado();
        this.dinheiroNaoEconomizado();

        try {
            List<Cigarros> cList = cigarroDAO.queryForAll();
            System.out.println("cList" + cList);
            if(cList.isEmpty()) {
                cigarrosNaoFumados.setText(Integer.toString(0));
                System.out.println("1111");
            }else{
                for(int i = 0; i < cList.size(); i++){
                    System.out.println("2222");
                    Calendar cDate = Calendar.getInstance();
                    Calendar atualDate = Calendar.getInstance();
                    cDate.setTime(cList.get(0).getDate());

                    if((cDate.get(Calendar.YEAR) == atualDate.get(Calendar.YEAR)) && (cDate.get(Calendar.DAY_OF_YEAR) == atualDate.get(Calendar.DAY_OF_YEAR))){
                        System.out.println("3333");
                        if(cigarros.getCigarrosDiario() < UserManager.getUser().getCigarros()){
                            cigarrosNaoFumados.setText(Integer.toString(UserManager.getUser().getCigarros()-cigarros.getCigarrosDiario()));
                        }else{
                            System.out.println("4444");
                            cigarrosNaoFumados.setText(Integer.toString(0));
                        }
                    }else {
                        System.out.println("555");
                        cigarrosNaoFumados.setText(Integer.toString(0));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void grafico(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Cigarros");

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Dias da semana");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Cigarros");
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(11);
        graph.getGridLabelRenderer().setNumHorizontalLabels(9);
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(8);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(pontos()));
        graph.addSeries(series);

        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
            //new DataPoint(0, 1),
            //new DataPoint(1, 5),
            //new DataPoint(2, 3),
            //new DataPoint(3, 2),
            //new DataPoint(4, 6)
        //});
        //graph.addSeries(series);

        //LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(generateDataMedia(Media()));
        //graph.addSeries(series2);

        /*LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        graph.addSeries(series2);*/

        series.setTitle("Você");
        series.setColor(Color.RED);
        //series2.setTitle("Pessoas");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public int[] pontos(){
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("day of week: " + calendar.get(Calendar.DAY_OF_WEEK));

        //calendar.add(Calendar.DAY_OF_WEEK, -1);
        int pontsGrafico[] = new int[7];

        int j = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("j:" + j);
        for (int i = 0; i < 7; i++){
            if(i == j) {
                pontsGrafico[i] = cigarros.getCigarrosDiario();
                System.out.println("cigarros:" + cigarros.getCigarrosDiario());
                System.out.println("pontsgrauc: " + pontsGrafico[i]);
                System.out.println("i:" + i);
            }
        }
        return pontsGrafico;
    }

    public DataPoint[] generateData(int[] pontsGraf){
        Calendar calendar = Calendar.getInstance();
        int count = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("Count: " + count);
        DataPoint[] values = new DataPoint[count+1];
        for (int i=0; i <= (count); i++) {
            System.out.println("i:" + i);
            //if(i == count) {
                DataPoint v = new DataPoint(i, pontsGraf[i]);
                System.out.println("v:" + v);
                values[i] = v;
                System.out.println("values: " + values[i]);
            //}
        }
        return values;
    }

    public float[] Media(){
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("day of week: " + calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        float media[] = new float[7];

        int j = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("j:" + j);
        for (int i = 0; i < 7; i++){
            if(i == j) {

                try {
                    WSMediaCigarros wsMediaCigarros = new WSMediaCigarros(calendar.getTime());
                    int m = wsMediaCigarros.execute().get();
                    System.out.println("media de retorno: " + m);
                    media[i] = m;
                    System.out.println("media cigarros:" + m);
                    System.out.println("media: " + media[i]);
                    System.out.println("i:" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
        return media;
    }

    public DataPoint[] generateDataMedia(float[] media){
        Calendar calendar = Calendar.getInstance();
        int count = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("Count: " + count);

        DataPoint[] values = new DataPoint[count];
        for (int i=0; i < (count); i++) {
            System.out.println("i:" + i);
            DataPoint v = new DataPoint(i, media[i]);
            System.out.println("v:" + v);
            values[i] = v;
            System.out.println("values: " + values[i]);
        }
        return values;
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
            Intent i = new Intent(this, Orientacao.class);
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


    public void dinheiroEconomizado() {
        float valorPorCigarro = 0, economizado = 0;
        valorPorCigarro = Float.parseFloat(UserManager.getUser().getValorMaco()) / 20;
        System.out.println("valor por cigarro: " + valorPorCigarro);
        int cigFum = cigarros.getCigarrosDiario();
        System.out.println("cigFum: " + cigFum);
        int nFumados = UserManager.getUser().getCigarros() - cigFum;
        System.out.println("nFumados: " + nFumados);

        if(nFumados > 0){
            economizado = economizado + (nFumados * valorPorCigarro);
            System.out.println("economizado" + economizado);
            dinheiroEconomizado.setText("R$ " + economizado );
        }else{
            dinheiroNaoEconomizado();
            System.out.println("entrou no else");
        }
    }

    public void dinheiroNaoEconomizado(){
        float valorPorCigarro = 0, nEconomizado=0;
        valorPorCigarro = Float.parseFloat(UserManager.getUser().getValorMaco())/20;
        System.out.println("valor por cigarro: " + valorPorCigarro);
        int cigFum = cigarros.getCigarrosDiario();
        System.out.println("cigFum: " + cigFum);
        int Fumados = UserManager.getUser().getCigarros() - cigFum;
        System.out.println("Fumados: " + Fumados);

        if(Fumados < 0){
            Fumados = Math.abs(Fumados);
            nEconomizado = nEconomizado + (Fumados * valorPorCigarro);
            System.out.println("economizado" + nEconomizado);
            dinheiroNaoEconomizado.setText("R$ " + nEconomizado );
        }
    }


    public void addCigarro(View v) throws SQLException {
        Calendar cal = Calendar.getInstance();
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

        dinheiroEconomizado();
        dinheiroNaoEconomizado();
        pontos();
        //grafico();
        pontoCigarroDiario();

        System.out.println("DADOS data: " + cigarros.getDate().getTime());
        System.out.println("DADOS cigarros: " + cigarros.getCigarrosDiario());

        Date d = new Date(1497063621913l * 1000);
        System.out.println("DADOS data: " + d);

        WSCigarro webServiceCigarro = new WSCigarro(cigarros.getDate(), cigarros.getCigarrosDiario(), UserManager.getUser().getEmail());//, UserManager.getUser());
        webServiceCigarro.execute();

    }

    public void subCigarro(View v) throws SQLException {
        Calendar cal = Calendar.getInstance();
        if (cigarros.getCigarrosDiario() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Você ainda não fumou cigarros hoje!");
        } else {
            if (cigarros.getCigarrosDiario() != 0) {
                cigarros.setCigarrosDiario(cigarros.getCigarrosDiario() - 1);
                cigarrosFumados.setText(Integer.toString(cigarros.getCigarrosDiario()));
                cigarros.setDate(cal.getTime());
                try {
                    cigarroDAO.update(cigarros);
                    //cigarroDAO.createOrUpdate(cigarros);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        dinheiroEconomizado();
        dinheiroNaoEconomizado();
        pontos();
        grafico();
        pontoCigarroDiario();

        WSCigarro webServiceCigarro = new WSCigarro(cigarros.getDate(), cigarros.getCigarrosDiario(), UserManager.getUser().getEmail());//, UserManager.getUser());
        webServiceCigarro.execute();
    }

    public void botaoNaoFumou(View view){
        Calendar cal = Calendar.getInstance();
        cigarros.setNaoFumou(true);
        cigarros.setDate(cal.getTime());
        try {
            cigarroDAO.update(cigarros);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pontoSemFumar();

        WSNaoFumou wsNaoFumou = new WSNaoFumou(UserManager.getUser().getEmail());
        wsNaoFumou.execute();
    }


    public void onPause() {
        super.onPause();
        grafico();
        dbsqLite.close();
    }

    public void posicao(){
        WSPosicao wsPosicao = new WSPosicao(UserManager.getUser().getEmail());
        try {
            int pos = wsPosicao.execute().get();
            posicao.setText(String.valueOf(pos));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    public void pontoSemFumar(){
        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Não fumar");
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

                acaoDAO.create(acao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pontoCigarroDiario(){
        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Informar diariamente a quantidade de cigarros fumados");
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

                acaoDAO.create(acao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
        Métodos do esquema de pontuação
     */

    public int visitaSite() {
        //acao = new Acao();
        //desafio = new Desafios();
        int ponto;

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visitar o site do Viva Sem Tabaco");
            System.out.println("Size de listDesafios" + desafiosList.size());
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId());
            System.out.println("Size de listAcao" + acaoList.size());
            if (acaoList.isEmpty()) {
                System.out.println("Nao fez acao de não fumar ainda");
            } else {
                System.out.println("Ganha ponto");

                for (Acao ac : acaoList) {
                    System.out.println("ponto" + ac.getPonto());
                    System.out.println("desafio" + ac.getDesafio().getTitulo());
                    System.out.println("user" + ac.getUser().getEmail());
                    System.out.println("data" + ac.getData());

                    List<User> userList = null;

                    userList = userDAO.queryForEq("id", ac.getUser().getId());


                    User user = userList.get(0);

                    System.out.println("USER EMAIL: " + user.getEmail());
                    System.out.println("USER Manager: " + UserManager.getUser().getEmail());

                    if (user.getEmail().equals(UserManager.getUser().getEmail())) {

                        System.out.println("ponto: " + ac.getPonto());
                        //if (ac.getUser().getEmail() == UserManager.getUser().getEmail()) {
                        ponto = ac.getPonto();
                        return ponto;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void varreVetor() {
    }

    public int verDicas() throws SQLException {

        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visualizar dicas diariamente");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            if (acaoList.isEmpty()) {
                System.out.println("Nao fez acao de não fumar ainda");
            } else {
                System.out.println("Ganha ponto");
                for (Desafios d : desafiosList) {
                    Acao ac = acaoList.get(0);

                    List<User> userList = null;
                    try {
                        userList = userDAO.queryForEq("id", ac.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    User user = userList.get(0);

                    System.out.println("USER EMAIL: " + user.getEmail());
                    System.out.println("USER Manager: " + UserManager.getUser().getEmail());

                    if (user.getEmail().equals(UserManager.getUser().getEmail())) {

                        if(ac.getData().equals(Calendar.getInstance().getTime())){
                            //ele já fez a acao de hoje, então nao computa mais ponto
                            System.out.println("e p entrar aqui");
                            pontoDicas = pontos.getPontoDica();
                        }
                        else{
                            pontoDicas = ac.getPonto();
                            System.out.println("pontoDica:" + pontoDicas);

                            if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                                //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                                Calendar c = Calendar.getInstance();
                                c.setTime(ac.getData());

                                dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                                if ((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))) { //ele ficou sem fumar ontem
                                    pontoDicas = pontoDicas + d.getVariacao();
                                }
                            }
                        }

                    }


                }
            }

            return pontoDicas;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int ficarSemFumar() {
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Não fumar");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            if (acaoList.isEmpty()) {
                System.out.println("Nao fez acao de não fumar ainda");
            } else {
                System.out.println("Ganha ponto");
                for (Desafios d : desafiosList) {

                    Acao ac = acaoList.get(0);

                    List<User> userList = null;
                    try {
                        userList = userDAO.queryForEq("id", ac.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    User user = userList.get(0);

                    System.out.println("USER EMAIL: " + user.getEmail());
                    System.out.println("USER Manager: " + UserManager.getUser().getEmail());

                    if (user.getEmail().equals(UserManager.getUser().getEmail())) {
                        pontoFicarSemFumar = ac.getPonto();
                        System.out.println("Ponto fumar: " + pontoFicarSemFumar);
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if ((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))) { //ele ficou sem fumar ontem
                            pontoFicarSemFumar = pontoFicarSemFumar + d.getVariacao();
                        }
                    }
                }
                return pontoFicarSemFumar;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void preencherPlano(){

    }

    public int fazerCadastroApp() {
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Fazer cadastro no aplicativo");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            if (acaoList.isEmpty()) {
                System.out.println("Nao fez acao de cadastro ainda");
            } else {
                System.out.println("Ganha ponto");
                for (Desafios d : desafiosList) {

                    Acao ac = acaoList.get(0);

                    List<User> userList = null;
                    try {
                        userList = userDAO.queryForEq("id", ac.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    User user = userList.get(0);

                    System.out.println("USER EMAIL: " + user.getEmail());
                    System.out.println("USER Manager: " + UserManager.getUser().getEmail());

                    if (user.getEmail().equals(UserManager.getUser().getEmail())) {

                        pontoCadastroApp = ac.getPonto();
                        System.out.println("pontoApp: " + pontoCadastroApp);

                        if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                            //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                            System.out.println("Nao deve entrar aqui");
                            Calendar c = Calendar.getInstance();
                            c.setTime(ac.getData());

                            dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                            if ((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))) { //ele ficou sem fumar ontem
                                pontoCadastroApp = pontoCadastroApp + d.getVariacao();
                            }
                        }
                    }
                    return pontoCadastroApp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void completarCadastro(){

    }

    public void completarFagestron(){

    }

    public int registroDiario(){
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Informar diariamente a quantidade de cigarros fumados");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametroi
            if(acaoList.isEmpty()){
                System.out.println("Nao fez acao de registro ainda para ganhar ponto");
            }else {
                System.out.println("Ganha ponto");
                for (Desafios d : desafiosList) {

                    Acao ac = acaoList.get(0);

                    List<User> userList = null;
                    try {
                        userList = userDAO.queryForEq("id", ac.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    User user = userList.get(0);

                    System.out.println("USER EMAIL: " + user.getEmail());
                    System.out.println("USER Manager: " + UserManager.getUser().getEmail());

                    if (user.getEmail().equals(UserManager.getUser().getEmail())) {
                        pontoRegistroDiario = ac.getPonto();
                        System.out.println("Ponto registro: " + pontoRegistroDiario);
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if ((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))) { //ele ficou sem fumar ontem
                            pontoRegistroDiario = pontoRegistroDiario + d.getVariacao();
                        }
                    }
                }
                return pontoRegistroDiario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int retornaPontuacaoTotal() throws SQLException {

        Pontos pontos = new Pontos();

        List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visualizar dicas diariamente");
        Desafios d = desafiosList.get(0);
        List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", d.getId());
        for(int i = 0; i < acaoList.size(); i++){
            if(acaoList.get(i).getData().equals(Calendar.getInstance().getTime())){
                pDica = pontos.getPontoDica();
            }
            else {
                pDica = this.verDicas();
                pontos.setPontoDica(pDica);
            }
        }

        List<Desafios> desafiosList1 = desafioDAO.queryForEq("titulo", "Informar diariamente a quantidade de cigarros fumados");
        Desafios d1 = desafiosList1.get(0);
        List<Acao> acaoList1 = acaoDAO.queryForEq("desafio_id", d1.getId());
        for(int i = 0; i < acaoList1.size(); i++){
            if(acaoList1.get(i).getData().equals(Calendar.getInstance().getTime())){
                pRegistro = pontos.getPontoRegistro();
            }
            else {
                pRegistro = this.registroDiario();
                pontos.setPontoRegistro(pRegistro);
            }
        }

        List<Desafios> desafiosList2 = desafioDAO.queryForEq("titulo", "Não fumar");
        Desafios d2 = desafiosList2.get(0);
        List<Acao> acaoList2 = acaoDAO.queryForEq("desafio_id", d2.getId());
        for(int i = 0; i < acaoList2.size(); i++){
            if(acaoList2.get(i).getData().equals(Calendar.getInstance().getTime())) {
                pNaoFumar = pontos.getPontoNaoFumar();
            }
            else{
                pNaoFumar = this.ficarSemFumar();
                pontos.setPontoNaoFumar(pNaoFumar);
            }
        }

        List<Desafios> desafiosList3 = desafioDAO.queryForEq("titulo", "Visitar o site do Viva Sem Tabaco");
        Desafios d3 = desafiosList3.get(0);
        List<Acao> acaoList3 = acaoDAO.queryForEq("desafio_id", d3.getId());
        for(int i = 0; i < acaoList3.size(); i++){
            if(acaoList3.get(i).getDesafio().getTitulo().equals(d3.getTitulo())){
                pSite = pontos.getPontoSite();
            }
            else{
                pSite = this.visitaSite();
                pontos.setPontoSite(pSite);
            }
        }

        List<Desafios> desafiosList4 = desafioDAO.queryForEq("titulo", "Fazer cadastro no aplicativo");
        Desafios d4 = desafiosList4.get(0);
        List<Acao> acaoList4 = acaoDAO.queryForEq("desafio_id", d4.getId());
        for(int i = 0; i < acaoList4.size(); i++) {
            System.out.println("iiii: " + acaoList4.size());
            System.out.println("iiii: " + acaoList4.get(i).getDesafio().getTitulo());
            System.out.println("iiii: " + acaoList4.get(i).getData());

                if(acaoList4.get(i).getData() != null) {
                    List<Pontos> p = pontoDAO.queryForAll();
                    Pontos pnt = p.get(0);
                    if (pnt.getPontoCadastroApp() != 0) {

                        System.out.println("teste: " + acaoList4.get(i).getDesafio().getTitulo());
                        System.out.println("teste: " + d4.getTitulo());
                        pCadastroApp = d4.getPontuacao();
                        System.out.println("Tem que vir aqui");
                    } else {
                        pCadastroApp = this.fazerCadastroApp();
                        pontos.setPontoCadastroApp(pCadastroApp);
                    }
                }


            System.out.println("Teste: " + acaoList4.get(i).getDesafio().getTitulo());
            System.out.println("Teste: " + d4.getTitulo());
           // if (acaoList4.get(i).getDesafio().getTitulo().equals(d4.getTitulo())) {
            //    pCadastroApp = pontos.getPontoCadastroApp();
            //} else {
            //    pCadastroApp = this.fazerCadastroApp();
            //}
        }

        System.out.println("pDica: " + pDica);
        System.out.println("pSite: " + pSite);
        System.out.println("pCadastro: " + pCadastroApp);
        System.out.println("pNaoFumar: " + pNaoFumar);
        System.out.println("olha: " + UserManager.getUser().getEmail());

        int pontoTotal = pDica + pSite + pCadastroApp + pRegistro + pNaoFumar;

        pontos.setUser(UserManager.getUser());

        try {
            pontoDAO.createOrUpdate(pontos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ponto total : " + pontoTotal);

        List<Pontos> pontosList = pontoDAO.queryForAll();
        for(int i = 0; i < pontosList.size(); i++){
            //System.out.println("Dao ponto: " + pontosList.get(i).getPonto());
            System.out.println("Dao ponto: " + pontosList.get(i).getUser().getName());
        }

        WSPonto wsPonto = new WSPonto(pontos.getPontoDica(),pontos.getPontoCadastroApp(), pontos.getPontoRegistro(), pontos.getPontoNaoFumar(), pontos.getPontoSite(), UserManager.getUser().getEmail());
        wsPonto.execute();
        return pontoTotal;

    }




}
