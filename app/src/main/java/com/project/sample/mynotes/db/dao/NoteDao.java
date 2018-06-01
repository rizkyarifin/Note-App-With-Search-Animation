package com.project.sample.mynotes.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.project.sample.mynotes.db.entities.Note;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Note ORDER BY created_at DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM Note WHERE note_id IN (:noteId)")
    Note getNoteById(String noteId);

    @Query("SELECT * FROM Note WHERE title LIKE (:filter) OR note LIKE (:filter)")
    List<Note> getNoteByFilter(String filter);

    @Insert(onConflict = REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
