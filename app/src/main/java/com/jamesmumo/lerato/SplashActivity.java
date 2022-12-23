package com.jamesmumo.lerato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.jamesmumo.lerato.screens.MainActivity;
import com.jamesmumo.lerato.screens.Onboarding;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime = sharedPreferences.getString("FirstTimeInstall", "");

        if (firstTime.equals("Yes")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 0);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, Onboarding.class);
                    startActivity(intent);
                    finish();
                }
            }, 0);
        }


    }
}

//Todo: Bottom navbar that u pull
// Todo: Left Navbar
// Todo: Notifivations
//Todo: slide back the nav drawer
