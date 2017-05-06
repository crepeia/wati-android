package com.example.daniela.progresso.Entidade;

import android.util.Log;

import com.facebook.AccessToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by daniela on 25/04/17.
 */


   /* public static final String tabela = "user";
    public static final String campo_id = "id";
    public static final String campo_name = "name";
    public static final String campo_email = "email";
    public static final String campo_cigarros = "cigarros";
    public static final String campo_valorMaco = "valorMaco";



    public Long id;
    public String name;
    public String email;
    public int cigarros;
    public float valorMaco;
*/
@DatabaseTable (tableName = "user")
public class User implements Serializable{
    @DatabaseField (columnName = "id", generatedId = true)//(allowGeneratedIdInsert=true, generatedId=true)
    private int id;

    @DatabaseField (columnName = "name")
    private String name;

    @DatabaseField  (columnName = "email")
    private String email;

    @DatabaseField (columnName = "cigarros")
    private Integer cigarros;

    @DatabaseField (columnName = "valor_Maco")
    private String valorMaco;

    @DatabaseField (columnName = "gender")
    private String gender;

    public User(){}

    public User(int id, String name, String email, Integer cigarros, String valorMaco, String gender){
        this.id = id;
        this.name = name;
        this.email = email;
        this.cigarros = cigarros;
        this.valorMaco = valorMaco;
        this.gender = gender;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCigarros() {
        return cigarros;
    }

    public void setCigarros(Integer cigarros) {
        this.cigarros = cigarros;
    }

    public String getValorMaco() {
        return valorMaco;
    }

    public void setValorMaco(String valorMaco) {
        this.valorMaco = valorMaco;
    }

    public String getGender() { return gender;}

    public void setGender(String gender) { this.gender = gender; }

    public User loggedUser(User user){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            Log.i("Saída: ", user.getName());
            return user;
            //tem alguem logado
        }

        else
            Log.i("Saída: ", " Usuário não logado");
        return null;

    }
}
