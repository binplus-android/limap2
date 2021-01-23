package com.limap.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.limap.R;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    ImageView photo;
    String photo_url="";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photo = findViewById(R.id.photo);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("PHOTO");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        photo_url = bundle.getString("photo_url");
        Log.d("photo_url",photo_url);
        Picasso.with(getApplicationContext())
                .load(photo_url)
                .into(photo);
        //Glide.with(getApplicationContext()).load(photo_url).into(photo);
    }
}
