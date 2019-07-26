package com.shaary.notes.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;


import com.shaary.notes.Model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("select * from note_table order by priority desc")
    LiveData<List<Note>> getAllNotes();

    @RawQuery(observedEntities = Note.class)
    LiveData<List<Note>> getSortedNotes(SupportSQLiteQuery query);
}
