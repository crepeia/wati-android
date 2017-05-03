package com.example.daniela.progresso;

import android.content.Intent;
import android.util.Log;

import com.example.daniela.progresso.Entidade.User;
import com.facebook.AccessToken;

/**
 * Created by daniela on 02/05/17.
 */

public class UserManager {

    static User user;
    private static boolean loggedIn = false;



    public static User logFacebook(User usr){

        user = usr;

       // super.onStart();
        AccessToken acessToken = AccessToken.getCurrentAccessToken();
        if (acessToken != null){
            loggedIn = true;
            Log.i("LoggedIn", String.valueOf(loggedIn));
            Log.i("Usuario: ", user.getName());

        }

        return usr;

    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserManager.user = user;
    }

}
