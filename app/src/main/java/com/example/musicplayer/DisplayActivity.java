package com.example.musicplayer;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class DisplayActivity extends AppCompatActivity {
    MediaPlayer music = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        Context context = DisplayActivity.this;
        ProgressBar progressBar = findViewById(R.id.progress_Bar);
        TextView time_info = findViewById(R.id.time_info);

        //获取传入信息
        int time = getIntent().getIntExtra("time",0);
        int currentPosition = getIntent().getIntExtra("currentPosition", -1);
        int position = getIntent().getIntExtra("Position", -1);
//        String[] nameStr =  getIntent().getStringArrayExtra("nameStr");
        int[] idStr = getIntent().getIntArrayExtra("idStr");
        boolean playMode = getIntent().getBooleanExtra("playMode", false);

        //播放逻辑
//        if((position != currentPosition) || !playMode){
//            music.reset();
//            music = MediaPlayer.create(context,idStr[position]);
//            music.start();
//        }



        Integer Time = time/1000;
        time_info.setText(Time.toString());
        progressBar.setMax(time);

//        Timer timer = new Timer("progress");

    }

}
