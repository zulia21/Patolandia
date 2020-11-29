package com.example.patolandis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class BancoSQLite extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "Patolandia";

    public BancoSQLite(Context context) {
        super(context, NOME_BANCO, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (Cod_Usuario INT primary key , Login TEXT, Nome TEXT,  Senha TEXT, Apelido TEXT, DataNasc TEXT, Celular TEXT, FotodoPerfil TEXT)");
    }

    public void onInsertNew (String Login, String Nome, String Senha, String Apelido, String DataNascimento, String Celular, String Foto, SQLiteDatabase db)
    {
        db.execSQL("insert into Usuario(rowid, Login, Nome, Senha, Apelido, DataNasc, Celular, FotodoPerfil) values (null, ?, ?, ?, ?, ?, ?, ?);", new String[]{Login, Nome, Senha, Apelido, DataNascimento, Celular, Foto});
    }
    public void onSelectLogin( SQLiteDatabase db)
    {
        db.execSQL("select Login from Usuario");
    }
    public void onSelectSenha (SQLiteDatabase db)
    {
        db.execSQL("select Senha from Usuario");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
