package com.aleksey.kurs_work;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aleksey.kurs_work.db.DatabaseHelper;
import com.aleksey.kurs_work.db_service.DataBaseService;
import com.aleksey.kurs_work.models.Note;

import java.util.Objects;

/** Код активити предназначенной для создания записей */
public class NoteActivity extends AppCompatActivity {
    /** Виджет, предназначенный для записи текста */
    private EditText editText;
    /** Виджет-кнопка, предназначенная для возвращения на главную активити */
    private ImageButton goOutButton;
    /** Объект-запись, контент которой находится на данной активи в данный момент */
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        getWidgets();
        getData();
        fillingEditText(note);
        addFunctionality();
    }

    /** Вспомогательный метод, который загружает в код необходимые виджеты */
    private void getWidgets() {
        editText = findViewById(R.id.et_note_text);
        goOutButton = findViewById(R.id.ib_gu_out);
    }

    /** Вспомогательнвй метод, который получает объект записи,
     *  на которую кликнул пользователь в главной активити */
    private void getData() {
        note = (Note) getIntent().getSerializableExtra("note");
    }

    /**
     * Вспомогательный метод, который заполняет контентом при условии:
     * Если запись существовала, то она заполняется сохранённым контентом
     * @param note Объект, содержащий контент записи
     */
    private void fillingEditText(Note note) {
        if (!Objects.isNull(note)) {
            editText.setText(note.getText());
        }
    }

    /** Вспомогательный метод, который добавляет фуекциональность некоторым виджетам */
    private void addFunctionality() {
        goOutButton.setOnClickListener(view -> {
            noteUpdating();
            finish();
        });
    }

    /** Вспомгательный метод, который добавляет запись в базу данных, если эта запись новая
     *  Или обновляет запись в базе данных, если запись уже существовала и при этом пользователь
     *  поменял её контент
     *  */
    private void noteUpdating() {
        if (Objects.isNull(note)) {
            final String text = editText.getText().toString();
            if (!text.isEmpty()) {
                final DatabaseHelper helper = new DatabaseHelper(NoteActivity.this);
                final DataBaseService service = new DataBaseService(helper);

                service.addNote(text);
            }
        } else {
            final String text = editText.getText().toString();
            final String previousText = note.getText();

            if (!text.equals(previousText)) {
                final DatabaseHelper helper = new DatabaseHelper(NoteActivity.this);
                final DataBaseService service = new DataBaseService(helper);

                final int id = note.getId();

                service.updateNote(id, text);
            }
        }
    }
}