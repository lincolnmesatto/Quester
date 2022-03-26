package com.pucpr.quester.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pucpr.quester.model.entity.Instituicao;

import java.util.ArrayList;

public class InstituicaoDatabase extends SQLiteOpenHelper{
    private static final String DB_NAME = "quester";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "instituicao";
    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_CIDADE = "cidade";
    private static final String COL_ESTADO = "estado";
    private static final String COL_EMAIL = "email";

    private Context context;

    public InstituicaoDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;

        query = String.format("CREATE TABLE IF NOT EXISTS %s("+
                " %s INTEGER PRIMARY KEY AUTOINCREMENT, "+
                " %s TEXT, " +
                " %s INTEGER)",DB_TABLE,COL_ID,COL_NOME,COL_CIDADE,COL_ESTADO,COL_EMAIL);

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public ArrayList<Instituicao> retrieveInstituicaoFromDB(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DB_TABLE,null,null,
                null,null,null,null);
        ArrayList<Instituicao> instituicoes = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String nome = cursor.getString(cursor.getColumnIndex(COL_NOME));
                String estado = cursor.getString(cursor.getColumnIndex(COL_ESTADO));
                String cidade = cursor.getString(cursor.getColumnIndex(COL_CIDADE));
                String email = cursor.getString(cursor.getColumnIndex(COL_EMAIL));

                Instituicao i = new Instituicao(id,nome,estado,cidade,email);
                instituicoes.add(i);

            }while (cursor.moveToNext());
        }
        database.close();
        return instituicoes;
    }
    public long createInstituciaoInDB(Instituicao instituicao){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME,instituicao.getNome());
        values.put(COL_CIDADE,instituicao.getCidade());
        values.put(COL_ESTADO,instituicao.getEstado());
        values.put(COL_EMAIL,instituicao.getEmail());
        long id = database.insert(DB_TABLE,null,values);
        database.close();
        return id;
    }

    public long updateInstituicaoInDB(Instituicao instituicao){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME,instituicao.getNome());
        values.put(COL_CIDADE,instituicao.getCidade());
        values.put(COL_ESTADO,instituicao.getEstado());
        values.put(COL_EMAIL,instituicao.getEmail());
        long i = database.update(DB_TABLE,values, COL_ID+" = ? ", new String[]{String.valueOf(instituicao.getId())});
        database.close();
        return i;
    }

    public long deleteInstituicaoInDB(long id){
        SQLiteDatabase database = getWritableDatabase();
        long i = database.delete(DB_TABLE, COL_ID+" = ? ", new String[]{String.valueOf(id)});
        database.close();
        return  i;
    }
}

