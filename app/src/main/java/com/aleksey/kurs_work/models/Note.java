package com.aleksey.kurs_work.models;

import java.io.Serializable;

/** Модель записи в дневнике */
public class Note implements Serializable {
    /** Уникальный идентификатор */
    private final int id;
    /** Заголовок записи */
    private String title;

    /** Полный текст записи */
    private String text;
    /** Время создания/обновления записи */
    private String updateDatetime;

    /** Конструктор с учётом id */
    public Note(int id, String title, String text, String updateDatetime) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.updateDatetime = updateDatetime;
    }

    /** Конструктор без учёта id */
    public Note(String title, String text, String updateDatetime) {
        this.id = -1;
        this.title = title;
        this.text = text;
        this.updateDatetime = updateDatetime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }
}
