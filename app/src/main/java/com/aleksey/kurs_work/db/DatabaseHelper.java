package com.aleksey.kurs_work.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Класс предоставляющий доступ к базе данных */
public class DatabaseHelper extends SQLiteOpenHelper {
    /** Имя базы данных */
    private static final String DATABASE_NAME = "my_diary";
    /** Версия базы данных */
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Метод, инициализирующий таблицу */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String strSQL = "CREATE TABLE IF NOT EXISTS my_diary (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(100) NOT NULL," +
                "text TEXT NOT NULL," +
                "update_datetime TEXT NOT NULL" +
                ")";

        db.execSQL(strSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
