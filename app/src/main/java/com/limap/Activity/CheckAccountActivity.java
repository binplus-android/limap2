package com.limap.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.limap.Pref.Pref;
import com.limap.R;

public class CheckAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk_account);
        if(Pref.getInstance(getApplicationContext()).getUserId().equals(""))
        {
            finish();
            Intent target = new Intent(getApplicationContext(), OTPActivity.class);
            startActivity(target);
        }
        else
        {
            finish();
            Intent target = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(target);
        }
    }
}
