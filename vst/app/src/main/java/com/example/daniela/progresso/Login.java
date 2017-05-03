package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.daniela.progresso.DAO.DBSQLite;
import com.example.daniela.progresso.DAO.UserDAO;
import com.example.daniela.progresso.Entidade.User;
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

import java.sql.SQLException;
import java.util.Arrays;


public class Login extends AppCompatActivity {

    private DBSQLite dbsqLite;
    private UserDAO userDAO;
    private User user;
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbsqLite = new DBSQLite(Login.this);
        try {
            userDAO = new UserDAO(dbsqLite.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        user = new User();


        FacebookSdk.setApplicationId("1436518353045731");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);


                if(user.getCigarros() != null && user.getValorMaco() != null){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getApplicationContext(), telaUnica.class);
                    startActivity(i);
                }
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
                            user.setName(firstName);
                            user.setEmail(email);
                            user.setGender(gender);


                            Log.i("Login" + "Id", id);

                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);

                            Log.i("LogX" , user.getName());
                            Log.i("LogX" , user.getEmail());
                            Log.i("LogX" , user.getGender());
                            Log.i("LogX" , String.valueOf(user.getId()));
                            //UserManager.logFacebook(user);
                            UserManager.setUser(UserManager.logFacebook(user));

                            userDAO.create(user);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        dbsqLite.close();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void onStart(){
        super.onStart();
        AccessToken acessToken = AccessToken.getCurrentAccessToken();
        if (acessToken != null){
            Intent i = new Intent(getApplicationContext(), telaUnica.class);
            startActivity(i);
        }
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


