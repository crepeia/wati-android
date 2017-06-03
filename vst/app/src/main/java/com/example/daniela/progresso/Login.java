package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.User;
import com.example.daniela.progresso.ws.WebServiceTask;
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
import java.util.List;


public class Login extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private UserDAO userDAO;
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Executando webService");
       // new WebServiceTask().execute();

        //usr = new User();
        dbsqLite = new DBSQLite(Login.this);

        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
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
                //User user = UserManager.getUser();
                setFacebookData(loginResult);

              /*  User user = UserManager.getUser();

                Log.i("Manager1: ", user.getName());
                Log.i("Manager1: ", user.getEmail());



                try {
                    userDAO.create(user);//verificar se é novo
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dbsqLite.close();

                redirecionarUsuario(user);*/

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

                            //Log.i("Manager2: ", UserManager.getUser().getName());
                            //Log.i("Manager2: ", UserManager.getUser().getEmail());

                            user.setName(firstName);
                            user.setEmail(email);
                            user.setGender(gender);

                           // Log.i("Manager3: ", user.getName());
                            //Log.i("Manager3: ", user.getEmail());

                            UserManager.setUser(user);
                            //Log.i("Manager4: ", UserManager.getUser().getName());
                            //Log.i("Manager4: ", UserManager.getUser().getEmail());


                            //Log.i("Login" + "Id", id);
/*
                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);

                            Log.i("LogX" , user.getName());
                            Log.i("LogX" , user.getEmail());
                            Log.i("LogX" , user.getGender());
                            Log.i("LogX" , String.valueOf(user.getId()));*/
                            //UserManager.logFacebook(user);
                            //UserManager.setUser(UserManager.logFacebook(user));


                            //Log.i("Manager1: ", UserManager.getUser().getName());
                            //Log.i("Manager1: ", UserManager.getUser().getEmail());


                            try {
                                List<User> usrLista = userDAO.queryForEq("email", user.getEmail());
                                if(usrLista.isEmpty())
                                    userDAO.create(user);//verificar se é novo
                                else
                                    userDAO.update(user);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dbsqLite.close();

                            redirecionarUsuario(user);


                        } catch (JSONException e) {
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

    public void buscarEmailVST(View v){

    }




    /*public static User loggedUser(User user){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            Log.i("Saída: ", user.getName());
            return user;
            //tem alguem logado
        }

        else
            Log.i("Saída: ", " Usuário não logado");
            return null;

    }*/





}


