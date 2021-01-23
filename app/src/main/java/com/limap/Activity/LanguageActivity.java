package com.limap.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.limap.Model.LocaleHelper;
import com.limap.Pref.Pref;
import com.limap.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    Button skip;
    CardView marathi;
    CardView hindi;
    CardView gujrati;
    CardView english;
    Context context;
    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("English","en");
                context = LocaleHelper.setLocale(LanguageActivity.this, "en");
                //resources = context.getResources();
            }
        });

        marathi = findViewById(R.id.marathi);
        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguage("Marathi","mr");
                context = LocaleHelper.setLocale(LanguageActivity.this, "mr");

            }
        });

        hindi = findViewById(R.id.hindi);
        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguage("Hindi","hi");
                context = LocaleHelper.setLocale(LanguageActivity.this, "hi");
            }
        });

        gujrati = findViewById(R.id.gujrati);
        gujrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguage("Gujrati","gu");
                context = LocaleHelper.setLocale(LanguageActivity.this, "gu");

            }
        });

        english = findViewById(R.id.english);
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguage("English","en");
                context = LocaleHelper.setLocale(LanguageActivity.this, "en");
            }
        });

    }

    private void setLanguage(String language,String code) {

        myLocale = new Locale(code);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Pref.getInstance(getApplicationContext()).setLANGUAGE(language);
        Pref.getInstance(getApplicationContext()).setCODE(code);
        finish();

        Intent target = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(target);
    }
}