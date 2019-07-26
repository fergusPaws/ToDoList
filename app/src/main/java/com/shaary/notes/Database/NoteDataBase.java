package com.shaary.notes.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.shaary.notes.Model.Note;

@Database(entities = {Note.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class NoteDataBase extends RoomDatabase {
    //Holds reference to the database
    private static NoteDataBase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDataBase.class, "room_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    //Used for testing to populate the brand new db with 3 items
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        private NoteDao noteDao;
//
//        private PopulateDbAsyncTask(NoteDataBase db) {
//            this.noteDao = db.noteDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            noteDao.insert(new Note("Title1", "Desc1", 1));
//            noteDao.insert(new Note("Title2", "Desc2", 2));
//            noteDao.insert(new Note("Title3", "Desc3", 3));
//            return null;
//        }
//    }
}
