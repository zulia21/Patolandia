package com.example.patolandis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText login, senha;

    public final static String EXTRA_MESSAGE_LOGIN ="com.example.patolandis.LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login =(EditText) findViewById(R.id.edtlogincad);
        senha = (EditText) findViewById(R.id.edtsenha);

    }
    public void Logar (View view)
    {
        BancoSQLite patolandia = new BancoSQLite(this);
        SQLiteDatabase db = this.openOrCreateDatabase(BancoSQLite.NOME_BANCO, MODE_PRIVATE, null);
        patolandia.onCreate(db);
        Cursor cursor;
        cursor = db.rawQuery("select * from Usuario where Login = ? and Senha  = ?;",new String[]{login.getText().toString(), senha.getText().toString()}, null);

        while (cursor.moveToNext())
        {
            if (String.valueOf(login.getText()).equals(cursor.getString(1)) && String.valueOf(senha.getText()).equals(cursor.getString(3))) {
                Intent intent = new Intent(this, PrincipalActivity.class);
                intent.putExtra(EXTRA_MESSAGE_LOGIN, cursor.getString(4));
                startActivity(intent);
                login.getText().clear();
                senha.getText().clear();
            }
        }
        cursor.close();




    }
    public void Duckar(View view)
    {
     Uri uri = Uri.parse("https://duckduckgo.com/");
     Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
     startActivity(intent);

    }
    public void cadastrarnovo(View view)
    {

        Intent intent = new Intent(this, NonCadastrado.class);
        startActivity(intent);
    }
}