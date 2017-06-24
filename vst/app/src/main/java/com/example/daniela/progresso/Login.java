package com.example.daniela.progresso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.daniela.progresso.DAO.AcaoDAO;
import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.DesafioDAO;
import com.example.daniela.progresso.DAO.DicasDAO;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.Acao;
import com.example.daniela.progresso.Entidade.Desafios;
import com.example.daniela.progresso.Entidade.User;
import com.example.daniela.progresso.ws.WSFacebook;
import com.example.daniela.progresso.ws.WSLoginSite;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private UserDAO userDAO;
    private DesafioDAO desafioDAO;
    private AcaoDAO acaoDAO;
    LoginButton loginButton;
    User usr;
    Acao acao;
    Desafios desafio;

    CallbackManager callbackManager;

    EditText textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textEmail = (EditText) findViewById(R.id.emailVST);

        //System.out.println("Executando webService");
        //new WebServiceTask().execute();


        //usr = new User();
        dbsqLite = new DBSQLite(Login.this);

        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        usr = new User();
        desafio = new Desafios();
        acao = new Acao();

        try {
            salvaDesafios();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        FacebookSdk.setApplicationId("1436518353045731");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        loginButton = (LoginButton) findViewById(R.id.tutorial_button_login);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void setFacebookData(final LoginResult loginResult)
    {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());

//VERIFICAR SE O USUÁRIO JÁ EXISTE NA LISTA DE USUÁRIOS DO BANCO

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");


                            Profile profile = Profile.getCurrentProfile();
                            //String id = profile.getId();
                            //String link = profile.getLinkUri().toString();
                            //Log.i("Link",link);

                            String id = object.getString("id");

                            String url = "https://graph.facebook.com/"+id+"/picture?type=small";


                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }
                            //User user = UserManager.getUser();

                            User user = new User();

                            user.setName(firstName);
                            user.setEmail(email);
                            user.setGender(gender);

                            UserManager.setUser(user);
                            Log.i("Manager4: ", UserManager.getUser().getName());
                            Log.i("Manager4: ", UserManager.getUser().getEmail());



                            try {
                                List<User> usrLista = userDAO.queryForEq("email", user.getEmail());
                                if(usrLista.isEmpty())
                                    userDAO.create(user);//verificar se é novo
                                else
                                    userDAO.update(user);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            WSFacebook webServiceFacebook = new WSFacebook(user.getName(), user.getEmail(), user.getGender());
                            webServiceFacebook.execute();

                            System.out.println("começando");
                            /*List<Acao> acaoList = acaoDAO.queryForEq("user_id", UserManager.getUser().getId());
                            System.out.println("1111111111");
                            System.out.println(UserManager.getUser().getId());
                            System.out.println(acaoList.size());
                            for(Acao a : acaoList) {
                                System.out.println("22222222");
                                if (a.getUser().getEmail() == UserManager.getUser().getEmail()) {
                                    System.out.println("3333333");
                                    if (a.getDesafio().getDescricao() == "Fazer cadastro no app") {
                                        System.out.println("Usuário já ganhou ponto por fazer cadstro no app");
                                    } else {
                                        System.out.println("Não ganhu ponto");
                                        contabilizaAcaoCadastroApp();
                                    }
                                }
                            }*/
                            List<Acao> acaoList = acaoDAO.queryForAll();
                            if(acaoList.isEmpty()){
                                System.out.println("acaodao vazio");
                                contabilizaAcaoCadastroApp();
                            }else{
                                System.out.println("acaodao diferente de vazio");
                            }


                            dbsqLite.close();
                            redirecionarUsuario(user);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void redirecionarUsuario(User user){
        if(user.getCigarros() != null && user.getValorMaco() != null){
            Log.i("ValorCigarros:", String.valueOf(user.getCigarros()));
            Log.i("ValorMaco:", String.valueOf(user.getValorMaco()));
            Log.i("Valorname:", String.valueOf(user.getName()));

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        else{
            Log.i("ValorCigarros:", String.valueOf(user.getCigarros()));
            Log.i("ValorMaco:", String.valueOf(user.getValorMaco()));
            Log.i("Valorname:", String.valueOf(user.getName()));
            Intent i = new Intent(getApplicationContext(), telaUnica.class);
            startActivity(i);
        }
    }


    protected void onStart(){
        super.onStart();
     //   AccessToken acessToken = AccessToken.getCurrentAccessToken();
      //  if (acessToken != null){
        dbsqLite = new DBSQLite(Login.this);

        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
            List<User> list= userDAO.queryForAll();
            //User u = userDAO.queryForAll().get(0);//listar usuário online

            if(!list.isEmpty()){

                UserManager.setUser(list.get(0));
                Log.i("Usuário logado: ", UserManager.getUser().getName());
               // Log.i("Managerrrrrrrrrrr: ", UserManager.getUser().getEmail());
                //Intent i = new Intent(getApplicationContext(), telaUnica.class);
                //startActivity(i);

                redirecionarUsuario(UserManager.getUser());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loginVST(View v){
        setContentView(R.layout.login_vst);
    }

    public void buscarEmailVST(View v) throws ExecutionException, InterruptedException, SQLException {

        System.out.println("AAAAAAAAAAAAA");
//        String x = textEmail.getText().toString();
        //System.out.println("AAAAAAAAAAAAA" + x);

        WSLoginSite ws = new WSLoginSite("cwsdanipereira@gmail.com");
        User user;
        ws.execute();
        user = ws.get();


        UserManager.setUser(user);
        System.out.println("User de retorno: " + user);
        System.out.println("User de retorno: " + user.getName());
        System.out.println("User de retorno: " + user.getGender());
        System.out.println("User de retorno: " + user.getEmail());

        List<User> usrLista = userDAO.queryForEq("email", user.getEmail());
        if(usrLista.isEmpty()) {
            System.out.println("É novo");
            userDAO.create(user);//verificar se é novo
            System.out.println("É novo");
        }else {
            System.out.println("Não É novo");
            userDAO.update(user);
            System.out.println("Não É novo");
        }

        redirecionarUsuario(user);
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
                desafio.setTitulo(titulo[i]);
                desafio.setDescricao(descricao[i]);
                desafio.setPontuacao(vetorPontuação[i]);
                desafio.setTipo(vetorTipo[i]);
                desafio.setVariacao(vetorVariacao[i]);

                System.out.println(desafio.getTitulo());
                System.out.println(desafio.getDescricao());
                System.out.println(desafio.getTipo());
                System.out.println(desafio.getVariacao());

                try {
                    System.out.println("create");
                    desafioDAO.create(desafio);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("DAO desafios diferente de vazio");
            System.out.println("desafio: " + desafioDAO);

        }
    }

    public void contabilizaAcaoCadastroApp(){
        //User user = new User();

        System.out.println("user: " + UserManager.getUser());
        System.out.println("userEmail: " + UserManager.getUser().getEmail());
        try {

            desafioDAO = new DesafioDAO(dbsqLite.getConnectionSource());
            acaoDAO = new AcaoDAO(dbsqLite.getConnectionSource());

            List<Desafios> desafiosList = desafioDAO.queryForEq("titulo", "Fazer cadastro no aplicativo");
            for (Desafios d : desafiosList){
                System.out.println(d.getTitulo());
                System.out.println(d.getDescricao());
                System.out.println(d.getPontuacao());
                System.out.println(d.getTipo());
                System.out.println(d.getVariacao());

                acao.setPonto(d.getPontuacao());
                acao.setUser(UserManager.getUser());
                acao.setData(Calendar.getInstance().getTime());
                acao.setDesafio(desafiosList.get(0));

                System.out.println("desafiosList.get(0)" + desafiosList.get(0).getTitulo());
                System.out.println("UserManager.getUser()" + UserManager.getUser().getEmail());
                acaoDAO.createOrUpdate(acao);
                List<Acao> acaoList = acaoDAO.queryForAll();
                for (Acao a : acaoList){
                    System.out.println("dao: " + a.getUser());
                    System.out.println("dao: " + a.getUser().getId());
                    System.out.println("dao: " + a.getUser().getEmail()); //me retorna null
                    System.out.println("dao: " + a.getDesafio());
                    System.out.println("dao: " + a.getDesafio().getTitulo()); //me retorna null
                    System.out.println("dao: " + a.getData());
                    System.out.println("dao: " + a.getPonto());

                }


            }//else{      É CONTINUA, ENTAO CALCULAR OS PONTOS PARA CONTINUA.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ACAO: " + acao.getUser().getEmail());
        System.out.println("ACAO: " + acao.getData());
        System.out.println("ACAO: " + acao.getDesafio().getTitulo());
        System.out.println("ACAO: " + acao.getPonto());

        }
}


