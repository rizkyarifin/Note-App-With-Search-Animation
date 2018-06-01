package com.project.sample.mynotes.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public abstract class BaseActivity extends AppCompatActivity {

    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait ..");
    }

    public static void showProgressDialog(){
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void hideProgressDialog(){
        mProgressDialog.dismiss();
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void removeFocus(View v){
        if (v != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }
}
