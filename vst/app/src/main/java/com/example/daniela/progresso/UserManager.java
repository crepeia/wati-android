package com.example.daniela.progresso;

import com.example.daniela.progresso.Entidade.User;
import com.facebook.AccessToken;


/**
 * Created by daniela on 02/05/17.
 */

public class UserManager {

    public static User user;
   // private static boolean loggedIn = false;

    public static User getUser() {

        //if(user == null)
          //  user = new User();
        return user;
    }

    public static void setUser(User user) {
        UserManager.user = user;
    }

}


   /* public static User logFacebook(final LoginResult loginResult, final Context context){


            // super.onStart();
            AccessToken acessToken = AccessToken.getCurrentAccessToken();
            Log.i("Response", String.valueOf(acessToken));
            Log.i("Response", String.valueOf(user));
            if (acessToken == null || (user == null)){


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("Response", response.toString());

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

                                    String url = "https://graph.facebook.com/" + id + "/picture?type=small";


                                    if (Profile.getCurrentProfile() != null) {
                                        Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                    }

                                    // verificar se usuario existe
                                    user = new User();
                                    loggedIn = true;
                                    Log.i("LoggedIn", String.valueOf(loggedIn));
                                    Log.i("Usuario: ", user.getName());

                                    user.setName(firstName);
                                    user.setEmail(email);
                                    user.setGender(gender);


                                    Log.i("Login" + "Id", id);

                                    Log.i("Login" + "Email", email);
                                    Log.i("Login" + "FirstName", firstName);
                                    Log.i("Login" + "LastName", lastName);
                                    Log.i("Login" + "Gender", gender);

                                    Log.i("LogX", user.getName());
                                    Log.i("LogX", user.getEmail());
                                    Log.i("LogX", user.getGender());
                                    Log.i("LogX", String.valueOf(user.getId()));
                                    //UserManager.logFacebook(user);

                                    //verificar se usuário ja existe, se existir atualizar caso contrario, criar!

                                    DBSQLite dbsqLite;
                                    UserDAO userDAO;

                                    dbsqLite = new DBSQLite(context);

                                    userDAO = new UserDAO(dbsqLite.getConnectionSource());
                                    userDAO.create(user);

                                    dbsqLite.close();


                                }catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,gender");
                request.setParameters(parameters);
                request.executeAndWait();
                //request.executeAsync();

            }

        return user;

    }*/


