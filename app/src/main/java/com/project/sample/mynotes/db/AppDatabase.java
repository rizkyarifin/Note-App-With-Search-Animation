package com.project.sample.mynotes.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.project.sample.mynotes.db.converter.DateTypeConverter;
import com.project.sample.mynotes.db.dao.NoteDao;
import com.project.sample.mynotes.db.entities.Note;

@TypeConverters({DateTypeConverter.class})
@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static AppDatabase mAppDatabase;

    public static AppDatabase getDatabase(final Context context) {
        if (mAppDatabase == null) {
            synchronized (AppDatabase.class) {
                if (mAppDatabase == null){
                    mAppDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "note_db").build();
                }
            }
        }
        return mAppDatabase;
    }
}
