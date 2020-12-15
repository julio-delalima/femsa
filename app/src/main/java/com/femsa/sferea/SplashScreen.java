package com.femsa.sferea;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.femsa.sferea.Login.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent().setClass(SplashScreen.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_SCREEN_DELAY);
    }
}