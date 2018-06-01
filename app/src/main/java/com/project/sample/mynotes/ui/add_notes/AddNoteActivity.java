package com.project.sample.mynotes.ui.add_notes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.project.sample.mynotes.R;
import com.project.sample.mynotes.base.BaseActivity;
import com.project.sample.mynotes.db.AppDatabase;
import com.project.sample.mynotes.db.dao.NoteDao;
import com.project.sample.mynotes.db.entities.Note;

import java.util.Date;

public class AddNoteActivity extends BaseActivity {

    TextInputEditText mInputTitle, mInputNote;
    Button btnSave;
    Toolbar mToolbar;

    Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        bindUI();
        setUpToolbar();
        getIntentDataIfNeed();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus(AddNoteActivity.this.getCurrentFocus());

                String title = mInputTitle.getText().toString();
                String note = mInputNote.getText().toString();

                mNote.setTitle(title);
                mNote.setNote(note);
                mNote.setCreatedAt(new Date());

                new saveNoteData(AppDatabase.getDatabase(AddNoteActivity.this).noteDao(),
                        mNote).execute();
            }
        });
    }

    void bindUI(){
        mToolbar = findViewById(R.id.toolbar);
        btnSave = findViewById(R.id.btn_submit);
        mInputTitle = findViewById(R.id.input_title);
        mInputNote = findViewById(R.id.input_note);
    }

    void getIntentDataIfNeed(){
        mNote = new Note();
        if (getIntent().getExtras() != null){
            mNote = (Note) getIntent().getExtras().getSerializable("note");

            mInputTitle.setText(mNote.getTitle());
            mInputNote.setText(mNote.getNote());
        }
    }

    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : {
                onBackPressed();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class saveNoteData extends AsyncTask<Void, Void, Void>{

        private final NoteDao mNoteDao;
        private final Note mNote;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        private saveNoteData(NoteDao mNoteDao, Note mNote) {
            this.mNoteDao = mNoteDao;
            this.mNote = mNote;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mNoteDao.insertNote(mNote);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            AddNoteActivity.this.setResult(RESULT_OK);
            AddNoteActivity.this.finish();
        }
    }
}
