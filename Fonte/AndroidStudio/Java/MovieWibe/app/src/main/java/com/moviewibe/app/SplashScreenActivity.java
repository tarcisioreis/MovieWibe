package com.moviewibe.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Tarcisio Machado dos Reis
 */
public class SplashScreenActivity extends AppCompatActivity implements Runnable{

    private final int splashTime = 5000;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        Handler h = new Handler();
        h.postDelayed(this, splashTime);
   }

    @Override
    public void run() {
        startActivity(new Intent(context, MovieListActivity.class));

        finish();
    }
}
