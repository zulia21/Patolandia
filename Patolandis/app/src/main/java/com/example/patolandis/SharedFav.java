package com.example.patolandis;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

public class SharedFav implements View.OnClickListener{
    boolean favorito;
    Context context;
    ImageView coracao;
    public  static String PREFERENCIAS = "com.example.patolandis";
    public String ATRACAO;

    SharedFav( Context context, ImageView coracao, String ATRACAO) {
        this.context = context;
        this.coracao = coracao;
        this.ATRACAO = ATRACAO;
        coracao.setOnClickListener(this);
        recuperar();
    }


    public void armazenar()
    {
        SharedPreferences config  = context.getSharedPreferences(PREFERENCIAS, 0);
        SharedPreferences.Editor editor = config.edit();
        editor.putBoolean(ATRACAO, favorito);
        editor.apply();

    }
    public void recuperar()
    {
        SharedPreferences config = context.getSharedPreferences(PREFERENCIAS, 0);
        favorito = config.getBoolean(ATRACAO, false);
        coracao.setImageResource(favorito?R.drawable.heartfull: R.drawable.heartempty);

    }


    @Override
    public void onClick(View v) {
        favorito = !favorito;
        if (!favorito) {
            coracao.setImageResource(R.drawable.heartempty);
        }
        else {
            coracao.setImageResource(R.drawable.heartfull);
        }
        armazenar();
    }
}

