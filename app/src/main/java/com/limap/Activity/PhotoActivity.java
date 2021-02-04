package com.limap.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.limap.Adapter.ViewPagerImageAdapter;
import com.limap.R;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    public ViewPager viewPager;
    ImageView pre;
    ImageView next;
    Bundle bundle;
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
        bundle = getIntent().getExtras();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
//        photo_url = bundle.getString("photo_url");
//        Log.d("photo_url",photo_url);
//        Picasso.with(getApplicationContext())
//                .load(photo_url)
//                .into(photo);
        //Glide.with(getApplicationContext()).load(photo_url).into(photo);

        viewPager = findViewById(R.id.pager);
        final ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(PhotoActivity.this,
                bundle.getString("image1"),
                bundle.getString("image2"),
                bundle.getString("image3"),
                bundle.getString("image5"),
                false);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                //actionBar.setSelectedNavigationItem(position);
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });

    }


}
