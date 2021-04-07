package com.example.musicplayer;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class DisplayActivity extends AppCompatActivity {
    static MediaPlayer music = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        Context context = DisplayActivity.this;
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
        TextView song_info = findViewById(R.id.song_info);

        //获取传入信息
//        int time = getIntent().getIntExtra("time",0);
        int currentPosition = getIntent().getIntExtra("currentPosition", -1);
        int position = getIntent().getIntExtra("position", -1);
        String[] nameStr =  getIntent().getStringArrayExtra("nameStr");
        int[] idStr = getIntent().getIntArrayExtra("idStr");
        boolean playMode = getIntent().getBooleanExtra("playMode", false);

        //播放逻辑
        if(playMode){
            music.reset();
            playMode = false;
        }
        if((position != currentPosition) || !playMode){
            Log.d("IN", "进入播放分支");
            music.reset();
            music = MediaPlayer.create(context,idStr[position]);
            music.start();
            playMode = true;
            int progressTime = music.getDuration();
            seekBar.setMax(progressTime/1000);
            time_info.setText(Integer.toString(progressTime/1000)+ " s");
            song_info.setText(nameStr[position]);
        }
    }

}
