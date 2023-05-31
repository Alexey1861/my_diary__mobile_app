package com.aleksey.kurs_work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.aleksey.kurs_work.db.DatabaseHelper;
import com.aleksey.kurs_work.db_service.DataBaseService;
import com.aleksey.kurs_work.models.Note;

import java.util.List;

/** Код главной активности */
public class MainActivity extends AppCompatActivity {
    /** Виджет-список с отсортированными по update_datetime записями из базы данных */
    private RecyclerView notesListView;
    /** Виджет-кнопка, которая открывает активность для создания записи */
    private ImageButton addNotesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgets();
        addFunctionality();
        recyclerViewSetUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshData();
    }

    /** Вспомогательный метод, который загружает в код необходимые виджеты */
    private void getWidgets() {
        notesListView = findViewById(R.id.rv_notes);
        addNotesButton = findViewById(R.id.b_add_note);
    }

    /** Вспомогательный метод, который добавляет функциональность некоторым виджетам */
    private void addFunctionality() {
        addNotesButton.setOnClickListener(view -> {
            final Intent intent = new Intent(MainActivity.this, NoteActivity.class);

            intent.putExtra("note", (Note) null);

            startActivity(intent);
        });
    }

    /** Вспомогательный метод, который настраивает виджет RecyclerView */
    private void recyclerViewSetUp() {
        notesListView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notesListView.setLayoutManager(linearLayoutManager);

        final NoteAdapter adapter = new NoteAdapter();
        notesListView.setAdapter(adapter);
    }

    /** Вспомогательный метод, который обновляет список записей на главной активити */
    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        final List<Note> notesList;

        try (final DatabaseHelper helper = new DatabaseHelper(this)) {
            final DataBaseService service = new DataBaseService(helper);
            notesList = service.getAllNotes();
        }

        NoteAdapter adapter = (NoteAdapter) notesListView.getAdapter();
        adapter.setNotesList(notesList);
        adapter.notifyDataSetChanged();
    }
}