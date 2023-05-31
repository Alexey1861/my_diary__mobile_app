package com.aleksey.kurs_work.db_service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aleksey.kurs_work.db.DatabaseHelper;
import com.aleksey.kurs_work.models.Note;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Класс предоставляющий абстракция над sql */
public class DataBaseService {
    /** Объект предоставляющий доступ к базе данных */
    private final DatabaseHelper databaseHelper;

    public DataBaseService(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Метод, который добавляет новую запись в базу данных
     * @param text Текст новой записи
     */
    public void addNote(final String text) {
        final String title = getTitleFromText(text);
        final String createDatetime = new Timestamp(System.currentTimeMillis()).toString();

        try (final SQLiteDatabase database = databaseHelper.getWritableDatabase()) {
            final ContentValues values = new ContentValues();

            values.put("title", title);
            values.put("text", text);
            values.put("update_datetime", createDatetime);

            final long newRowId = database.insert("my_diary", null, values);
            if (newRowId == -1) {
                throw new RuntimeException("Ошибка в добавлении записи в базу данных");
            }
        }
    }

    /**
     * Метод, который обновляет уже существующую базу запись
     * @param id Уникальный идентификатор записи, которую нужно обновить
     * @param text Новый текст записи
     */

    public void updateNote(final int id, final String text) {
        final String title = getTitleFromText(text);
        final String updateDatetime = new Timestamp(System.currentTimeMillis()).toString();

        try (final SQLiteDatabase database = databaseHelper.getWritableDatabase()) {
            final ContentValues values = new ContentValues();

            values.put("title", title);
            values.put("text", text);
            values.put("update_datetime", updateDatetime);

            final String whereCondition = "id = ?";
            final String[] whereArgs = {String.valueOf(id)};

            final long rowsUpdated = database.update("my_diary", values, whereCondition, whereArgs);
            if (rowsUpdated <= 0) {
                throw new RuntimeException("Ошибка в обновлении записей в базе данных");
            }
        }
    }

    /**
     * Метод, который удаляет запись из базы данных по её уникальному идентификатору
     * @param id Уникальный идентификатор
     */
    public void deleteNote(final int id) {
        try (final SQLiteDatabase database = databaseHelper.getWritableDatabase()) {
            final String whereCondition = "id = ?";
            final String[] whereArgs = {String.valueOf(id)};

            final int rowsDelete = database.delete("my_diary", whereCondition, whereArgs);
            if (rowsDelete <= 0) {
                throw new RuntimeException("Ошибка при удалении записей из базы данных");
            }
        }
    }

    /**
     * Метод, который возвращает список всех записей из базы данных в виде списка Note отсортированных по убыванию поля update_datetime
     * @return Список с записями
     */
    public List<Note> getAllNotes() {
        final String sqlQuery = "SELECT id, title, text, update_datetime FROM my_diary ORDER BY update_datetime DESC";

        try (final SQLiteDatabase database = databaseHelper.getReadableDatabase();
             final Cursor cursor = database.rawQuery(sqlQuery, null)) {

            final List<Note> notesList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") final int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") final String title = cursor.getString(cursor.getColumnIndex("title"));
                    @SuppressLint("Range") final String text = cursor.getString(cursor.getColumnIndex("text"));
                    @SuppressLint("Range") final String updateDatetime = cursor.getString(cursor.getColumnIndex("update_datetime"));

                    // Создать объект Note и добавить его в список notesList
                    final Note note = new Note(id, title, text, updateDatetime);
                    notesList.add(note);
                } while (cursor.moveToNext());
            }

            return notesList;
        }
    }

    /**
     * Вспомогательный метод, который формирует заголовок (если есть \n, то строка до него, иначе весь текст)
     * @param text Исходный текст записи
     * @return Заголовок
     */
    private String getTitleFromText(final String text) {
        final String regex = "(.*?)(?:\\n|$)";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Ошибка в нахождении title");
        }
    }
}
