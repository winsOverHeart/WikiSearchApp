package com.wikisearch.app.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wikisearch.app.R;
import com.wikisearch.app.util.CommonForAll;

public class SplashActivity extends CommonForAll {

    private ImageView logo_IV;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeViewVariable();
        initializeVariable();
        initializeView();
    }

    @Override
    public void initializeView() {
        logo_IV.startAnimation(mAnimation);

        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goNext(SplashActivity.this, WikiSearchContent.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void initializeViewVariable() {
        logo_IV = (ImageView) findViewById(R.id.logo_IV);
    }

    @Override
    public void initializeVariable() {
        mAnimation = AnimationUtils.loadAnimation(this,
                R.anim.fade);
    }
}
