package com.example.patolandis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

public class NonCadastrado extends AppCompatActivity {
    EditText login, nome, senha, apelido, data_de_nascimento, celular;
    ImageView FotodePerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_cadastrado);
        login = (EditText)findViewById(R.id.edtlogincad);
        nome = (EditText)findViewById(R.id.edtnome);
        senha = (EditText)findViewById(R.id.edtsenhacad);
        apelido = (EditText)findViewById(R.id.edtapelido);
        data_de_nascimento = (EditText)findViewById(R.id.edtdatanasc);
        celular = (EditText)findViewById(R.id.edtcelular);
        FotodePerfil = (ImageView)findViewById(R.id.imguser);
    }
    public void escolherFotodePerfil(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(RequestCode,ResultCode, data);
        if (ResultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            FotodePerfil.setImageURI(uri);
        }

    }
    public void cadastrar(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        String Nome = nome.getText().toString();
        String Login = login.getText().toString();
        String Senha = senha.getText().toString();
        String Apelido = apelido.getText().toString();
        String Data_Nascimento = data_de_nascimento.getText().toString();
        String Celular = celular.getText().toString();
        if (Nome.isEmpty() || Login.isEmpty() || Senha.isEmpty() || Apelido.isEmpty() || !Data_Nascimento.matches("\\d\\d/\\d\\d/\\d{4}") || Celular.isEmpty() )
        {
            nome.setError("O campo nome deve ser preenchido!");
            login.setError("O campo login deve ser preenchido!");
            senha.setError("O campo senha deve ser preenchido!");
            apelido.setError("O campo apelido deve ser preenchido!");
            data_de_nascimento.setError("O campo data de nascimento deve ser preenchido corretamente!");
            celular.setError("O campo celular deve ser preenchido!");
            return;
        }
        BancoSQLite patolandia = new BancoSQLite(this);
        SQLiteDatabase db = this.openOrCreateDatabase(BancoSQLite.NOME_BANCO, MODE_PRIVATE, null);
        patolandia.onCreate(db);
        patolandia.onInsertNew(Login, Nome, Senha, Apelido, Data_Nascimento, Celular, null , db);
        startActivity(intent);


    }
}