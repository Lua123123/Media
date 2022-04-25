package com.example.media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
//

    private static final int REQUEST_VIDEO = 123;
    String API_KEY = "AIzaSyAhF47wc8lWWTq6rjlYEik1xDYerPok3VY";
    YouTubePlayerView youTubePlayerView;
//    RecyclerView recyclerViewYoutube;
//    BottomNavigationView bottomNavYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

//        recyclerViewYoutube = findViewById(R.id.recycler_view_youtube);
//        bottomNavYoutube = findViewById(R.id.bottom_nav_youtube);
        youTubePlayerView = findViewById(R.id.myYoutube);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo("znC9jBVL3Ko");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(YoutubePlayerActivity.this, REQUEST_VIDEO);
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO) {
            youTubePlayerView.initialize(API_KEY, YoutubePlayerActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}