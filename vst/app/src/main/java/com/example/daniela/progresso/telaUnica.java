package com.example.daniela.progresso;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class telaUnica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_unica);
    }

    public void clicouSalvarCigarroMaco(View view){
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);
    }


}
