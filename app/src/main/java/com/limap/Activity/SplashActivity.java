package com.limap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.limap.Pref.Pref;
import com.limap.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        delayedCall();
    }

    private void delayedCall(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(Pref.getInstance(getApplicationContext()).getLANGUAGE().isEmpty()) {
                    Intent target = new Intent(getApplicationContext(), LanguageActivity.class);
                    startActivity(target);
                }else {

                    Intent target = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(target);
                }

                finish();

            }
        }, 0);

    }
}