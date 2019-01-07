package com.example.mosaab.news.ViewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mosaab.news.R;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread Splash = new Thread(){
            @Override
            public void run(){

                try {
                    sleep(2000); // the time of holding the splash
                    Intent splash = new Intent(Splash_screen.this,MainActivity.class);
                    startActivity(splash);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Splash.start();

    }
}
