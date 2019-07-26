package com.shaary.notes;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.shaary.notes.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.shaary.notes.EXTRA_DESC";
    public static final String EXTRA_PRIO = "com.shaary.notes.EXTRA_PRIO";
    public static final String EXTRA_CATEGORY = "com.shaary.notes.EXTRA_CATEGORY";
    public static final String EXTRA_DATE = "com.shaary.notes.EXTRA_DATE";
    public static final String EXTRA_ID = "com.shaary.notes.EXTRA_ID";
    private static final String TAG = AddNoteActivity.class.getSimpleName();

    private EditText noteTitle;
    private EditText noteDescription;
    private EditText noteCategory;
    private NumberPicker numberPicker;
    private Button dueDateButton;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteTitle = findViewById(R.id.edit_text_title);
        noteDescription = findViewById(R.id.edit_text_description);
        noteCategory = findViewById(R.id.edit_text_category);
        numberPicker = findViewById(R.id.number_picker_priority);
        dueDateButton = findViewById(R.id.due_date_button);


        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        //Populates the layout if user opens existing note
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            noteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            noteDescription.setText(intent.getStringExtra(EXTRA_DESC));
            noteCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIO, 1));
            Date date = (Date) intent.getSerializableExtra(EXTRA_DATE);
            if (date != null) {
                dueDate = date;
                dueDateButton.setText(formateDate(date));
            }
        } else {
            setTitle("Add Note");
        }

        dueDateButton.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            if (dueDate != null) {
                calendar.setTime(dueDate);
            }
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH);
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddNoteActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog,
                    onDateSetListener,
                    yearNow, monthNow, dayNow);
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.delete_date_button_text), (dialogInterface, i) -> {
                dueDateButton.setText(R.string.due_date_button_text);
                dueDate = null;
                dialog.dismiss();
            });
            dialog.show();
        });

        onDateSetListener = (datePicker, year, month, day) -> {
            dueDate = new GregorianCalendar(year, month, day).getTime();
            dueDateButton.setText(formateDate(dueDate));
        };
    }

    private void saveNote() {
        String title = noteTitle.getText().toString();
        String description = noteDescription.getText().toString();
        String category = noteCategory.getText().toString();
        int priority = numberPicker.getValue();

        //Checks if title empty and if yes, doesn't allow to save it
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert the title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, description);
        data.putExtra(EXTRA_PRIO, priority);
        data.putExtra(EXTRA_CATEGORY, category);
        data.putExtra(EXTRA_DATE, dueDate);

        //Checks if the note is already exists and if yes, sends the id to allow changes
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String formateDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
