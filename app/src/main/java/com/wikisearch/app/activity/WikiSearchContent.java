package com.wikisearch.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wikisearch.app.interfaces.ApiCall;
import com.wikisearch.app.adapter.CustomAdapter;
import com.wikisearch.app.R;
import com.wikisearch.app.model.ContentSource;
import com.wikisearch.app.util.CommonForAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WikiSearchContent extends CommonForAll implements CustomAdapter.ListItemClickListener {

    static View.OnClickListener myOnClickListener;
    private ProgressBar mProgressBar;
    private static RecyclerView.Adapter adapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerView;
    private static ArrayList<ContentSource> data;
    Call<JsonObject> call1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_search_content);
        initializeViewVariable();
        initializeVariable();
        initializeView();
    }

    @Override
    public void initializeViewVariable() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void initializeVariable() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void initializeView() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("WikiSearch");
        data = new ArrayList<>();
        adapter = new CustomAdapter(data, this, this);
        recyclerView.setAdapter(adapter);
        checkSharedPrefAndUpdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu, menu);
        MenuItem search_item = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (isNetworkAvailable())
                    if (!s.isEmpty())
                        getWikiQueryWithRetro(s);
                    else
                        Toast.makeText(WikiSearchContent.this, "Ensure network connectivity", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void getWikiQueryWithRetro(final String param) {
        mProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiCall.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiCall api = retrofit.create(ApiCall.class);
        if (call1 != null) {
            call1.cancel();
        }
        call1 = api.getGenericJSON(param);
        call1.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgressBar.setVisibility(View.GONE);
                data.clear();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Abc", response.body().toString());
                    try {
                        SharedPreferences appSharedPrefs = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(getList(response.body().toString()));
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();
//                        data.addAll(getList(response.body().toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    checkSharedPrefAndUpdate();
                } else {
                    Log.e("Abc", "Error in getGenericJson:" + response.code() + " " + response.message());
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call1, Throwable t) {
                Log.e("Abc", "Response Error");
            }
        });
    }

    protected void checkSharedPrefAndUpdate() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<ContentSource>>() {
        }.getType();
        List<ContentSource> mContentSources = gson.fromJson(json, type);
        if (mContentSources != null) {
            data.addAll(mContentSources);
            adapter.notifyDataSetChanged();
        }
    }

    public ArrayList<ContentSource> getList(String input) throws JSONException {
        ArrayList<ContentSource> contentList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        JSONObject jsonObject = new JSONObject(input);
        JSONArray obj = jsonObject.getJSONObject("query").getJSONArray("pages");
        for (int i = 0; i < obj.length(); i++) {
            ContentSource pageContent = gson.fromJson(String.valueOf(obj.get(i)), ContentSource.class);
            if (validateData(pageContent))
                contentList.add(pageContent);
        }
        return contentList;
    }

    private boolean validateData(ContentSource pageContent) {
        if (!TextUtils.isEmpty(pageContent.getTitle())
                && pageContent.getTerms() != null && pageContent.getTerms().getDescription() != null && !TextUtils.isEmpty(pageContent.getTerms().getDescription().get(0))
                && !TextUtils.isEmpty(pageContent.getFullurl())
                && pageContent.getOriginal() != null && !TextUtils.isEmpty(pageContent.getOriginal().getSource())
                && pageContent.getThumbnail() != null && !TextUtils.isEmpty(pageContent.getThumbnail().getSource()))
            return true;
        else
            return false;
    }


    @Override
    public void onItemClickListener(int clickedPosition) {


        Intent mIntent = new Intent(this, WikiSearchActivity.class);
        mIntent.putExtra("url", data.get(clickedPosition).getFullurl());
        mIntent.putExtra("name", data.get(clickedPosition).getTitle());
        startActivity(mIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

