package com.example.media;

// src/main/java/com/example/videoplayer/VideoPlayerActivity.java


import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class video extends AppCompatActivity {

    private VideoView videoView;

    private MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //textView.setText(intent.getStringExtra("current"));
        Uri uri = bundle.getParcelable("uri");
     //   mediaPlayer = MediaPlayer.create(this, uri);

        // Initialize views
        videoView = findViewById(R.id.videoView);


        // MediaController allows playback controls (rewind, fast forward, etc.)
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        // Set video URI (local or online)
       // Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
            }
        });


    }
    public void onBackPressed() {
        videoView.stopPlayback();
        super.onBackPressed();
        // Call the super class's onBackPressed method to handle the default back button behavior

    }
}
