package com.shaary.notes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.shaary.notes.Adapters.NoteAdapter;
import com.shaary.notes.Model.Note;
import com.shaary.notes.ViewModel.NoteViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SortDialog.onInputListener {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

    private NoteViewModel noteViewModel;
    private String sortType = "priority";
    private String sortOrder = "desc";

    private final NoteAdapter noteAdapter = new NoteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if user changed the sort method. Default is sort by priority descending.
        if (savedInstanceState != null) {
            String savedType = savedInstanceState.getString("type");
            String savedOrder = savedInstanceState.getString("order");
            if (savedType != null) {
                sortType = savedType;
            }
            if (savedOrder != null) {
                sortOrder = savedOrder;
            }
        }

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        uiUpdate(sortType, sortOrder);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //Gets and deletes the note at swiped position
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddNoteActivity.EXTRA_DESC, note.getDescription());
            intent.putExtra(AddNoteActivity.EXTRA_CATEGORY, note.getCategory());
            intent.putExtra(AddNoteActivity.EXTRA_PRIO, note.getPriority());
            intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddNoteActivity.EXTRA_DATE, note.getDueDate());
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        });
    }

    private void uiUpdate(String sortType, String sortOrder) {
        noteViewModel.getSortedNotes(sortType, sortOrder).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteAdapter.submitList(notes);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", sortType);
        outState.putString("order", sortOrder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESC);
            String category = data.getStringExtra(AddNoteActivity.EXTRA_CATEGORY);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIO, 1);
            Date date = (Date) data.getSerializableExtra(AddNoteActivity.EXTRA_DATE);


            Note note = new Note(title, description, category, priority, date);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESC);
            String category = data.getStringExtra(AddNoteActivity.EXTRA_CATEGORY);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIO, 1);
            Date date = (Date) data.getSerializableExtra(AddNoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, category, priority, date);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_notes:
                SortDialog dialog = new SortDialog();
                Bundle bundle = new Bundle();
                bundle.putString("type", sortType);
                bundle.putString("order", sortOrder);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "SortDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void sendInput(String type, String order) {
        sortType = type;
        sortOrder = order;
        uiUpdate(sortType, sortOrder);
    }
}
