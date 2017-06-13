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
import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Cigarros;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.Dicas;
import com.example.daniela.progresso.Entidade.Pontos;
import com.example.daniela.progresso.Entidade.User;
import com.example.daniela.progresso.ws.WSCigarro;
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
    private AcaoDAO acaoDAO;
    private DesafioDAO desafioDAO;
    private DicasDAO dicasDAO;
    private PontoDAO pontoDAO;
    Cigarros cigarros;
    User user;
    Acao acao;
    Dicas dicas;
    Desafios desafio;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataInsercaoCigarro = new ArrayList<>();

        dbsqLite = new DBSQLite(MainActivity.this);

        System.out.println("dbsqlite: " + dbsqLite);
        try {
            cigarroDAO = new CigarrosDAO(dbsqLite.getConnectionSource());
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
            dicasDAO = new DicasDAO(dbsqLite.getConnectionSource());
            pontoDAO = new PontoDAO(dbsqLite.getConnectionSource());

            System.out.println("cigarrroDAO: " + cigarroDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        pontuacao.setText(Integer.toString(this.retornaPontuacaoTotal()));

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

        grafico();
    }






    public void grafico(){
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

        DataPoint[] values = new DataPoint[count];
        for (int i=0; i < (count); i++) {
            System.out.println("i:" + i);
            DataPoint v = new DataPoint(i, pontsGraf[i]);
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
            dinheiroEconomizado.setText("R$ " + economizado + ",00");
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
            dinheiroNaoEconomizado.setText("R$ " + nEconomizado + ",00");
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

        System.out.println("DADOS data: " + cigarros.getDate().getTime());
        System.out.println("DADOS cigarros: " + cigarros.getCigarrosDiario());

        Date d = new Date(1497063621913l * 1000);
        System.out.println("DADOS data: " + d);

        //WSCigarro webServiceCigarro = new WSCigarro(cigarros.getDate(), cigarros.getCigarrosDiario(), UserManager.getUser().getEmail());//, UserManager.getUser());
        //webServiceCigarro.execute();

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
    }


    public void onPause() {
        super.onPause();
        grafico();
        dbsqLite.close();
    }

    public void pontuacao(){

    }

    public void posicao(){

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
            List<Desafios> desafiosList = desafioDAO.queryForEq("tituto", "Informar diariamente a quantidade de cigarros fumados");
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

    public int visitaSite(){
        //acao = new Acao();
        //desafio = new Desafios();
        int ponto;

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visitar o site do Viva Sem Tabaco");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId());

            for (Acao ac : acaoList){
                if(ac.getUser().getEmail() == UserManager.getUser().getEmail()){
                    ponto = ac.getPonto();
                    return ponto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int verDicas(){
        Calendar dtAtual = Calendar.getInstance();
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Visualizar dicas diariamente");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio", desafio); //retorna a ação que tem como desafio o passado como parametro
            for(Desafios d : desafiosList){

                for (Acao ac : acaoList) {
                    if (ac.getUser().getEmail() == UserManager.getUser().getEmail()) {
                        pontoDicas = ac.getPonto();
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))){ //ele leu dica no dia anterior tbem
                            pontoDicas = pontoDicas + d.getVariacao();
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


    public int ficarSemFumar(){
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Não fumar");
            desafio = desafiosList.get(0);

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            for(Desafios d : desafiosList){

                for (Acao ac : acaoList) {
                    if (ac.getUser().getEmail() == UserManager.getUser().getEmail()) {
                        pontoFicarSemFumar = ac.getPonto();
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))){ //ele ficou sem fumar ontem
                            pontoFicarSemFumar = pontoFicarSemFumar + d.getVariacao();
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

    public void preencherPlano(){

    }

    public int fazerCadastroApp(){
        Calendar dtAntes = Calendar.getInstance();
        acao = new Acao();
        desafio = new Desafios();

        try {
            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Fazer cadastro no aplicativo");
            desafio = desafiosList.get(0);
            System.out.println("desafio " + desafio.getDescricao());

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            System.out.println("id: " + desafio.getId());
            System.out.println("size: " + acaoList.size());
            for(Desafios d : desafiosList){
                System.out.println("*********");
                for (Acao ac : acaoList) {
                    System.out.println("///////");
                    System.out.println("ac.getUser().getEmail()" + ac.getUser().getEmail());
                    System.out.println(ac.getId());
                    System.out.println(ac.getUser());
                    System.out.println(ac.getData());
                    System.out.println(ac.getDesafio());
                    System.out.println(ac.getPonto());
                    System.out.println(ac.getUser().getEmail());
                    System.out.println(ac.getDesafio().getDescricao());
                    System.out.println("UserManager.getUser().getEmail()" + UserManager.getUser().getEmail());
                    if (ac.getUser().getEmail() == UserManager.getUser().getEmail()) {
                        pontoCadastroApp= ac.getPonto();
                        System.out.println("pontoApp: " + pontoCadastroApp);
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        System.out.println("Nao deve entrar aqui");
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))){ //ele ficou sem fumar ontem
                            pontoCadastroApp = pontoCadastroApp + d.getVariacao();
                        }
                    }
                }
            }
            return pontoCadastroApp;

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

            List<Acao> acaoList = acaoDAO.queryForEq("desafio_id", desafio.getId()); //retorna a ação que tem como desafio o passado como parametro
            for(Desafios d : desafiosList){

                for (Acao ac : acaoList) {
                    if (ac.getUser().getEmail() == UserManager.getUser().getEmail()) {
                        pontoRegistroDiario = ac.getPonto();
                    }

                    if (d.getTipo() == 1) { // 1 é contínua, então verifica se no dia anterior ele ganhou ponto.
                        //dtAtual.setTime(ac.getData()); //pega a data atual (data atual do dia requisição) do banco
                        Calendar c = Calendar.getInstance();
                        c.setTime(ac.getData());

                        dtAntes.add(Calendar.DAY_OF_YEAR, -1);
                        if((c.get(Calendar.DAY_OF_MONTH) == dtAntes.get(Calendar.DAY_OF_YEAR))){ //ele ficou sem fumar ontem
                            pontoRegistroDiario = pontoRegistroDiario + d.getVariacao();
                        }
                    }
                }
            }
            return pontoRegistroDiario;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int retornaPontuacaoTotal(){
        int pontoDica = 0, pontoSite = 0, pontoCadastroApp = 0, pontoRegistro = 0, pontoNaoFumar = 0;
        //pontoDica = this.visitaSite();
        //pontoSite = this.visitaSite();
        pontoCadastroApp = this.fazerCadastroApp();
        //pontoRegistro = this.registroDiario();
        //pontoNaoFumar = this.ficarSemFumar();

        int pontoTotal = pontoDica + pontoSite + pontoCadastroApp + pontoRegistro + pontoNaoFumar;

        Pontos pontos = new Pontos();
        pontos.setUser(UserManager.getUser());
        pontos.setPonto(pontoTotal);

        try {
            pontoDAO.createOrUpdate(pontos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ponto total : " + pontoTotal);
        return pontoTotal;

    }




}
