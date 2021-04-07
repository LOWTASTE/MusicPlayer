package com.example.musicplayer;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class DisplayActivity extends AppCompatActivity {
    static MediaPlayer music = new MediaPlayer();
    Context context = DisplayActivity.this;
    int currentPosition;
    int position;
    boolean playMode;
    //boolean isSeekbar_Chaning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 基本操作
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
        TextView song_info = findViewById(R.id.song_info);
        Button prev = findViewById(R.id.btn_prev);
        Button pause = findViewById(R.id.btn_pause);
        Button next = findViewById(R.id.btn_next);

        //获取传入信息
        currentPosition = getIntent().getIntExtra("currentPosition", -1);
        position = getIntent().getIntExtra("position", -1);
        String[] nameStr =  getIntent().getStringArrayExtra("nameStr");
        int[] idStr = getIntent().getIntArrayExtra("idStr");
        playMode = getIntent().getBooleanExtra("playMode", false);
        int total_num = getIntent().getIntExtra("total_num",0);

//        //播放逻辑已整合至play_music
//        if(playMode){
//            music.reset();
//            playMode = false;
//        }
//        if((position != currentPosition) || !playMode){
//            Log.d("IN", "进入播放分支");
//            music.reset();
//            music = MediaPlayer.create(context,idStr[position]);
//            music.start();
//            playMode = true;
//            int progressTime = music.getDuration();
//            seekBar.setMax(progressTime/1000);
//            time_info.setText(Integer.toString(progressTime/1000)+ " s");
//            song_info.setText(nameStr[position]);
//        }

        play_music(idStr,nameStr,position);


        //实现按钮事件
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music.isPlaying()){
                    music.pause();
                }
                else{
                    music.start();
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.reset();
                if (position - 1 < 0){
                    song_info.setText("没有上一首歌曲");
                    position = -1;
                }
                else {
                    play_music(idStr,nameStr,--position);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position + 1 >= total_num){
                    song_info.setText("没有下一首歌曲");
                    position = total_num;
                }
                else {
                    play_music(idStr,nameStr,++position);
                }
            }
        });


//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int duration2 = music.getDuration() / 1000;//获取音乐总时长
//                int position = music.getCurrentPosition();//获取当前播放的位置
//                time_info.setText(calculateTime(duration2));//总时长
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isSeekbar_Chaning = true;
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                isSeekbar_Chaning = false;
//                mediaPlayer.seekTo(seekBar.getProgress());//在当前位置播放
//                tv_start.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));
//            }
//        });

    }


    void play_music(int[] idStr,String[] nameStr,int position){
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
        TextView song_info = findViewById(R.id.song_info);
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
