package com.nicol.petcave;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nicol on 10/31/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "PetCaveDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE pets " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, " +
                "breed TEXT, " +
                "age INTEGER, " +
                "gender TEXT, " +
                "color TEXT )";
        db.execSQL(sql);
        sql = "CREATE TABLE tasks " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT, " +
                "petID INTEGER, " +
                "FOREIGN KEY (petID) REFERENCES pets(id) ON DELETE CASCADE )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tasks";
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS pets";
        db.execSQL(sql);

        onCreate(db);
    }

}
