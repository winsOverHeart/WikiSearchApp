package com.wikisearch.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.wikisearch.app.R;

public abstract class CommonForAll extends AppCompatActivity {


    private Intent mIntent;

    public SharedPreferences shref;
    public SharedPreferences.Editor editor;

    public abstract void initializeViewVariable();

    public abstract void initializeVariable();

    public abstract void initializeView();

    public void goNext(Context mContext, Class appCompatActivity) {
        mIntent = new Intent(mContext, appCompatActivity);
        startActivity(mIntent);
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    public SharedPreferences.Editor getSharedPrefrences()
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        return appSharedPrefs.edit();
    }


}
;