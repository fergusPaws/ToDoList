package com.shaary.notes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shaary.notes.Model.Note;
import com.shaary.notes.R;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    private onItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldNote, @NonNull Note newNote) {
            return oldNote.getId() == newNote.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldNote, @NonNull Note newNote) {
            return oldNote.getTitle().equals(newNote.getTitle()) &&
                    oldNote.getDescription().equals(newNote.getDescription()) &&
                    oldNote.getPriority() == newNote.getPriority() &&
                    oldNote.getId() == newNote.getId();
        }
    };


    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int position) {
        Note currentNote = getItem(position);
        noteHolder.onBind(currentNote);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle;
        private TextView noteDesc;
        private TextView notePrio;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.text_view_title);
            noteDesc = itemView.findViewById(R.id.text_view_desc);
            notePrio = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void onBind(Note note) {
            noteTitle.setText(note.getTitle());
            noteDesc.setText(note.getDescription());
            notePrio.setText(String.valueOf(note.getPriority()));
        }
    }
}
