package com.websarva.wings.android.weightmemoapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //データベースの名前とヴァージョン名を変数に入れておく。
    private static final String DATABASE_NAME = "weightmemodb";
    private static final int DATABASE_VERSION = 1;

    //DatabaseHelperのコンストラクタ。引数はContext(使うActivity名)とする。
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE weightmemo(" +
                "_id INTEGER PRIMARY KEY," +
                "date TEXT," +
                "weight TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
