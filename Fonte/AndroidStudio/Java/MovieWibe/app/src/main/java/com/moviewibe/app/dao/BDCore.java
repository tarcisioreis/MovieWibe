package com.moviewibe.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Tarcisio Machado dos Reis
 *
 */
public class BDCore extends SQLiteOpenHelper {
    private static final String NOME_BD = "moviewibe";
    private static final int VERSAO_BD = 1;


    public BDCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table if not exists genres(id integer not null, " +
                   "                                  name text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
        onCreate(bd);
    }

}
