package com.sharmas.golf_android;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sharmas.golf_android.common.AppPreferences;

/**
 * Created by user on 3/14/2017.
 */

public class Splash_Screen extends AppCompatActivity {

    private Runnable splashrunnable = null;
    private Handler udatehandler = new Handler();
    ProgressBar simpleProgressBar;
    private TextView tv_laod;
    int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_sc);

        simpleProgressBar=(ProgressBar)findViewById(R.id.simpleProgressBar);
        tv_laod=(TextView)findViewById(R.id.loading);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            simpleProgressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
        }
        try {
            splashrunnable = new Runnable() {
                @Override
                public void run() {
                    String email = AppPreferences.getInstance(getApplicationContext()).getFromStore("email");
                    if (email.length() == 0) {
                        lancherActivity(Golf_Landing.class);
                    } else {
                        lancherActivity(GolfStates.class);
                    }
//                    Intent splash = new Intent(getApplicationContext(), Golf_Landing.class);
//                    startActivity(splash);
//                    finish();

                }
            };
            udatehandler.postDelayed(splashrunnable, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setProgressValue(progress);
    }
    private void setProgressValue(final int progress) {
        tv_laod.setVisibility(View.VISIBLE);
        // set the progress
        simpleProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 20);

            }
        });
        thread.start();
    }
    private void lancherActivity(Class<?> cls) {

        Intent start=new Intent(this,cls);
        startActivity(start);
        finish();
    }
}
