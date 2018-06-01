package com.project.sample.mynotes.ui.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.sample.mynotes.R;
import com.project.sample.mynotes.base.BaseActivity;
import com.project.sample.mynotes.customview.StaggeredRecylerView;
import com.project.sample.mynotes.db.AppDatabase;
import com.project.sample.mynotes.db.entities.Note;
import com.project.sample.mynotes.ui.add_notes.AddNoteActivity;

import java.util.List;

public class MainActivity extends BaseActivity {

    StaggeredRecylerView mRecylerView;
    NotesAdapter mNotesAdapter;
    ImageButton mImgSearch, mImgClose;
    ConstraintLayout mRootView;
    Toolbar mToolbar;
    EditText mInputSearch;
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
        setUpToolbar();
        setUpList();
        setUpAnimations();

        mFab.setOnClickListener(view ->
                navigateToAddNotes()
        );

        mInputSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                removeFocus(MainActivity.this.getCurrentFocus());
                new getAllNoteList("%"+mInputSearch.getText().toString()+"%").execute();
                return true;
            }
            return false;
        });
    }

    void bindUI(){
        mImgSearch = findViewById(R.id.img_search);
        mRootView = findViewById(R.id.rootView);
        mToolbar = findViewById(R.id.toolbar);
        mImgClose = findViewById(R.id.img_close);
        mInputSearch = findViewById(R.id.input_search);
        mFab = findViewById(R.id.fab);
        mRecylerView = findViewById(R.id.rv_notes);
    }

    void setUpToolbar(){
        setSupportActionBar(mToolbar);
    }

    void setUpAnimations() {
        mImgSearch.setOnClickListener(view -> {
            updateConstraints(R.layout.activity_main_alt);
            mInputSearch.requestFocus();
        });

        mImgClose.setOnClickListener(view -> {
            removeFocus(MainActivity.this.getCurrentFocus());
            mInputSearch.setText("");
            updateConstraints(R.layout.activity_main);
            new getAllNoteList("").execute();
        });
    }

    void updateConstraints(@LayoutRes int id){
        ConstraintSet mContraintSet = new ConstraintSet();
        mContraintSet.clone(this, id);
        mContraintSet.applyTo(mRootView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ChangeBounds transition = new ChangeBounds();
//            transition.setInterpolator(new OvershootInterpolator());
            transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
            transition.setDuration(800);
            TransitionManager.beginDelayedTransition(mRootView, transition);
        }
    }

    void setUpList(){
        mNotesAdapter = new NotesAdapter();
        mRecylerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecylerView.setAdapter(mNotesAdapter);

        setRecylerLayoutAnimation(true);

        new getAllNoteList("").execute();
    }

    void setRecylerLayoutAnimation(boolean mustToSet){
        if(mustToSet) {
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,
                    R.anim.grid_layout_animation_from_bottom);
            mRecylerView.setLayoutAnimation(animation);
        } else {
            mRecylerView.setLayoutAnimation(null);
        }
    }

    void navigateToAddNotes(){
        startActivityForResult(new Intent(this, AddNoteActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            new getAllNoteList("").execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getAllNoteList extends AsyncTask<Void, Void, List<Note>>{

        private final String filter;

        private getAllNoteList(String filter) {
            this.filter = filter;
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if(filter.equals("")) {
                mNotesAdapter.setNoteList(AppDatabase.getDatabase(MainActivity.this).noteDao().getAllNotes());
            }
            else {
                mNotesAdapter.setNoteList(AppDatabase.getDatabase(MainActivity.this).noteDao().getNoteByFilter(filter));
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
//            hideProgressDialog();
//            if (notes.size() == 0){
//                mRecylerView.setLayoutAnimation(null);
//            }
            mNotesAdapter.notifyDataSetChanged();
            super.onPostExecute(notes);
        }
    }
}
