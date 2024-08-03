package com.example.media;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AudioPlayer extends AppCompatActivity {
private TextView textView,time,progresss;
private SeekBar seekBar;
private MediaPlayer mediaPlayer ;
private ImageView play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView =findViewById(R.id.textView);
        time = findViewById(R.id.time);
        progresss = findViewById(R.id.time2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        textView.setText(intent.getStringExtra("current"));
        Uri uri = bundle.getParcelable("uri");
        mediaPlayer = MediaPlayer.create(this, uri);

        time.setText(String.valueOf(
                mediaPlayer.getDuration()/60000 == 0? mediaPlayer.getDuration()/1000 +"sec": mediaPlayer.getDuration()/60000+" mins"
        ));

        seekBar = findViewById(R.id.seekBar2);
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progresss.setText(String.valueOf(mediaPlayer.getCurrentPosition()/60000 == 0? mediaPlayer.getCurrentPosition()/1000 +"sec" : mediaPlayer.getCurrentPosition()/60000 +" mins"));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(seekBar.getProgress());

                    }
                });

        Thread updateseek = new Thread(){

           @Override
           public void run(){
                int cp =0;
                try{
                    while(cp<=mediaPlayer.getCurrentPosition()){
                        cp = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(cp);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
               super.run();
           }
        };
        updateseek.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
        // Call the super class's onBackPressed method to handle the default back button behavior

    }

    public void nav (View v){
        play = findViewById(R.id.play);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            play.setImageResource(R.drawable.baseline_play_circle_24);
        }else {
            mediaPlayer.start();
            play.setImageResource(R.drawable.baseline_pause_circle_24);
        }
    }



}