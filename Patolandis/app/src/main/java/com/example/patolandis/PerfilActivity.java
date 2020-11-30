package com.example.patolandis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    ImageView imgperfil;

    TextView nome, apelido, celular, data_de_nascimento;

    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        nome = (TextView)findViewById(R.id.txtnomeperf);
        apelido = (TextView) findViewById(R.id.txtapelidoperf);
        celular = (TextView) findViewById(R.id.txtcelularperf);
        imgperfil = (ImageView) findViewById(R.id.imguserperf);
        data_de_nascimento = (TextView) findViewById(R.id.txtdataperf);
        SQLiteDatabase db = openOrCreateDatabase(BancoSQLite.NOME_BANCO, MODE_PRIVATE, null);
        Intent intent = getIntent();
        String Apelido = intent.getStringExtra(PrincipalActivity.APELIDO);
        cursor = db.rawQuery("select * from Usuario where Apelido = ?;",new String[]{Apelido}, null);
        while(cursor.moveToNext())
        {
            if (Apelido.equals(cursor.getString(4)))
            {
                imgperfil.setImageURI(Uri.parse(cursor.getString(7)));
                nome.setText(cursor.getString(2));
                apelido.setText(cursor.getString(4));
                celular.setText(cursor.getString(6));
                data_de_nascimento.setText(cursor.getString(5));
            }
        }

    }

}