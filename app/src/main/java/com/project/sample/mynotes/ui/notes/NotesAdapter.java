package com.project.sample.mynotes.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.sample.mynotes.R;
import com.project.sample.mynotes.db.AppDatabase;
import com.project.sample.mynotes.db.entities.Note;
import com.project.sample.mynotes.ui.add_notes.AddNoteActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> mNoteList;
    private Context mContext;
    private Date mDate;

    private SimpleDateFormat mCurrentDateFormat =
            new SimpleDateFormat("HH:mm aaa", Locale.getDefault());
    private SimpleDateFormat mPastDateFormat =
            new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    NotesAdapter() {
        mDate = new Date();
    }

    void setNoteList(List<Note> noteList) {
        if (mNoteList != null) {
            mNoteList.clear();
        }
        mNoteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_note,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(position);
    }

    @Override
    public int getItemCount() {
        if (mNoteList != null) {
            return mNoteList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        private final TextView mTxtTitle;
        private final TextView mTxtNote;
        private final TextView mTxtCreatedAt;
        private Note mNote;

        ViewHolder(View itemView) {
            super(itemView);

            mTxtTitle = itemView.findViewById(R.id.txt_title);
            mTxtNote = itemView.findViewById(R.id.txt_note);
            mTxtCreatedAt = itemView.findViewById(R.id.txt_created_at);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void setItem(int position) {
            mNote = mNoteList.get(position);
            mTxtTitle.setText(mNote.getTitle());
            mTxtNote.setText(mNote.getNote());

            Date mCreateAt = mNote.getCreatedAt();
            if (mCreateAt.getDay() == mDate.getDay() && mCreateAt.getMonth() == mDate.getMonth() &&
                    mCreateAt.getYear() == mDate.getYear()) {
                mTxtCreatedAt.setText(mCurrentDateFormat.format(mCreateAt));
            } else {
                mTxtCreatedAt.setText(mPastDateFormat.format(mCreateAt));
            }
        }

        @Override
        public void onClick(View view) {
            navigateToNotesForm(mNote);
        }

        @Override
        public boolean onLongClick(View view) {
            initAlertDialog(mNote, getPosition());
            return false;
        }
    }

    private void navigateToNotesForm(Note mNote) {
        Activity activity = (Activity) mContext;
        Intent intent = new Intent(mContext, AddNoteActivity.class);
        intent.putExtra("note", mNote);
        activity.startActivityForResult(intent, 0);
    }

    private void initAlertDialog(final Note mNote, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder
                .setMessage("Hapus note ini ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new deleteNote(mNote, position).execute();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @SuppressLint("StaticFieldLeak")
    private class deleteNote extends AsyncTask<Void, Void, List<Note>> {

        private final Note mNote;
        private final int position;

        private deleteNote(Note mNote, int position) {
            this.mNote = mNote;
            this.position = position;
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            AppDatabase.getDatabase(mContext).noteDao().deleteNote(mNote);
            return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            Toast.makeText(mContext, "Note telah terhapus !", Toast.LENGTH_SHORT).show();
            mNoteList.remove(position);
            notifyDataSetChanged();
            super.onPostExecute(notes);
        }
    }
}
