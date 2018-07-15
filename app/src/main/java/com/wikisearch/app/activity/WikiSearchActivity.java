package com.wikisearch.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wikisearch.app.R;
import com.wikisearch.app.util.CommonForAll;

import java.util.Objects;

public class WikiSearchActivity extends CommonForAll {

    private WebView webView_WV;
    private String url;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_search);
        initializeViewVariable();
        initializeVariable();
        initializeView();
    }

    @Override
    public void initializeViewVariable() {
        webView_WV = (WebView) findViewById(R.id.webView_WV);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public void initializeVariable() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("url")) {
                url = getIntent().getStringExtra("url");
                Objects.requireNonNull(getSupportActionBar()).setTitle(getIntent().getStringExtra("name"));
            }
        }
    }

    @Override
    public void initializeView() {
        webView_WV.loadUrl(url);
        webView_WV.setWebViewClient(new AppWebViewClients(mProgressBar));
        webView_WV.getSettings().setLoadsImagesAutomatically(true);
        webView_WV.getSettings().setJavaScriptEnabled(true);
        webView_WV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView_WV.loadUrl(getIntent().getStringExtra("url"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;

        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView_WV.canGoBack()) {
                        webView_WV.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
