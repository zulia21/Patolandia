package com.example.patolandis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText login;
    Boolean favorito;
    public final static String EXTRA_MESSAGE_LOGIN ="com.example.patolandis.LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login =(EditText) findViewById(R.id.edtlogin);

    }
    public void Logar (View view)
    {

            Intent intent = new Intent(this, PrincipalActivity.class);
            intent.putExtra(EXTRA_MESSAGE_LOGIN, login.getText().toString());
            startActivity(intent);


    }
    public void Duckar(View view)
    {
     Uri uri = Uri.parse("https://duckduckgo.com/");
     Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
     startActivity(intent);

    }
}