package com.limap.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.limap.R;

public class VideoActivity extends AppCompatActivity {

    VideoView video;
    String video_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video = findViewById(R.id.video);

        Bundle bundle = getIntent().getExtras();
        video_url = bundle.getString("video_url");
        Log.d("video_url",video_url);
        initializePlayer();
    }

    private void initializePlayer() {
        try {
            //video.setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"));
            MediaController controller = new MediaController(VideoActivity.this);
            controller.setMediaPlayer(video);
            controller.setAnchorView(video);
            video.setMediaController(controller);
            video.setVideoPath(video_url);
            video.requestFocus();
            video.start();
        }catch(Exception e) {
            Log.e("ERROR", ""+e);
        }
    }

    private void releasePlayer() {
        video.stopPlayback();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            video.pause();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}