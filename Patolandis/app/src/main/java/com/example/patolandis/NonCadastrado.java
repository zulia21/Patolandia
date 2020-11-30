package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NonCadastrado extends AppCompatActivity {

    EditText login, nome, senha, apelido, data_de_nascimento, celular;

    ImageView FotodePerfil;

    String urifoto = null;
    public final static int COD_IMAGEM_SAVE = 0;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, COD_IMAGEM_SAVE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }
    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(RequestCode,ResultCode, data);
        if (ResultCode == RESULT_OK) {
            Uri uri = data.getData();
            FotodePerfil.setImageURI(uri);
            urifoto = String.valueOf(uri);
            if (uri!= null)
            try {
                salvarImagemExt(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static final String IMAGEM_USER = "img_usuario";

    public void salvarImagemExt(@NonNull Uri uri) throws IOException {
        File local = new File(Environment.getExternalStorageState(), IMAGEM_USER );
        if (!local.exists())
        {
            if(!local.mkdir())
            {
                throw new IOException("Falha na criação da pasta. Imagem de perfil do usuário");
            }
        }
        int quantidade = local.list().length;

        File arquivo = new File(local, IMAGEM_USER + quantidade + 1);

        if (arquivo.exists())
        {
            return;
        }
        OutputStream os = new FileOutputStream(arquivo);
        InputStream is = getContentResolver().openInputStream(uri);
        byte[] buffer = new byte[4096];
        int size;
        while ((size = is.read(buffer)) != -1) {
            os.write(buffer, 0, size);
        }

        is.close();
        os.close();

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
        patolandia.onInsertNew(Login, Nome, Senha, Apelido, Data_Nascimento, Celular, urifoto , db);
        startActivity(intent);


    }
}