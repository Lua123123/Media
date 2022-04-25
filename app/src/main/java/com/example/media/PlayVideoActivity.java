package com.example.media;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.media.fragment.Fragment2;

public class PlayVideoActivity extends AppCompatActivity {

    ImageView reg_back;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.video_video);

        String pathVideo = getIntent().getStringExtra("path_video");
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(Uri.parse(pathVideo));
        videoView.start();

//        reg_back();
    }

//    private void reg_back() {
//        reg_back = (ImageView) findViewById(R.id.reg_back);
//        reg_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame, new Fragment2());
//                fragmentTransaction.commit();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}