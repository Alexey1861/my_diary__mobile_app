package com.aleksey.kurs_work;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aleksey.kurs_work.db.DatabaseHelper;
import com.aleksey.kurs_work.db_service.DataBaseService;
import com.aleksey.kurs_work.models.Note;

import java.util.List;

/** Класс-адаптер для RecycleView */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    /** Список с записями, которые будут отображаться на главной активити в виджете RecyclerView */
    private List<Note> notesList;

    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_holder, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /** Класс-холдер содержащий данные о записи (Является элементом виджета RecyclerView) */
    class NoteViewHolder extends RecyclerView.ViewHolder {
        /** Виджет-заголовок для записи */
        TextView notesTitle;
        /** Виджет, показвающий дату и время обновления */
        TextView notesUpdateDatetime;
        /** Виджет-кнопка, которая удаляет выбранную запись */
        ImageButton deleteButton;
        /** Объект, содержащий контент записи */
        Note note;

        @SuppressLint("NotifyDataSetChanged")
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            getWidgets();
            addFunctionality();
        }

        /** Метод, обновляющий контент холдера */
        public void bind(Note newNote) {
            this.note = newNote;

            String title = this.note.getTitle();
            String updateDatetime = this.note.getUpdateDatetime();

            notesTitle.setText(title);
            notesUpdateDatetime.setText(updateDatetime);
        }

        /** Вспомогательный метод, который загружает в код необходимые виджеты */
        private void getWidgets() {
            notesTitle = itemView.findViewById(R.id.tv_note_title);
            notesUpdateDatetime = itemView.findViewById(R.id.tv_update_datetime);
            deleteButton = itemView.findViewById(R.id.b_delete);
        }

        /** Вспомогательный метод, который добавляет функциональность некоторым виджетам */
        @SuppressLint("NotifyDataSetChanged")
        private void addFunctionality() {
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), NoteActivity.class);
                intent.putExtra("note", this.note);
                view.getContext().startActivity(intent);
            });

            deleteButton.setOnClickListener(view -> {
                try (final DatabaseHelper helper = new DatabaseHelper(itemView.getContext())) {
                    final DataBaseService service = new DataBaseService(helper);

                    final int id = note.getId();
                    service.deleteNote(id);

                    List<Note> noteList = service.getAllNotes();
                    NoteAdapter.this.setNotesList(noteList);

                    NoteAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
