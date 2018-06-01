package com.project.sample.mynotes.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;

    private String title;
    private String note;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public Note(){}

    public Note(String title, String note, Date createdAt) {
        this.title = title;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
