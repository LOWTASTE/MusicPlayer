package com.example.musicplayer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayActivity extends AppCompatActivity {
    static MediaPlayer music = new MediaPlayer();
    Context context = DisplayActivity.this;
    Timer timer;
    //    int currentPosition;
    int position;
    boolean playMode;
    boolean isSeekBarchaning;
    static int loopMode = 0;
    static int total_num;
    static int[] idStr;
    static String[] nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 基本操作
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
//       TextView song_info = findViewById(R.id.song_info);
        TextView now_info = findViewById(R.id.now_info);
        Button loopButton =findViewById(R.id.loopType);
        //Button playButton =findViewById(R.id.btn_pause);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //获取传入信息
//        currentPosition = getIntent().getIntExtra("currentPosition", -1);
        position = getIntent().getIntExtra("position", -1);
        nameStr =  getIntent().getStringArrayExtra("nameStr");
        idStr = getIntent().getIntArrayExtra("idStr");
        playMode = getIntent().getBooleanExtra("playMode", false);
        total_num = getIntent().getIntExtra("total_num",0);

        loopButton.setText("播完停止");

        play_music(idStr,nameStr,position);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration2 = music.getDuration() / 1000;//获取音乐总时长
                int position1 = music.getCurrentPosition()/1000;//获取当前播放的位置
                now_info.setText(calculateTime(position1 ));//开始时间
                time_info.setText(calculateTime(duration2));//总时长
                if (duration2==position1){
                    playMode = false;
                    if(loopMode==2){
                        music.reset();
                        position+=1;
                        if(position>=total_num){
                            position=0;
                        }
                        play_music(idStr,nameStr,position);
                    }
                    else if(loopMode == 1){
                        play_music(idStr, nameStr, position);
                    }
                    else if(loopMode==3){
                        music.reset();
                        int i= (int) (Math.random() * total_num);
                        if(i==position){
                            position+=1;
                        }
                        else{
                            position=i;
                        }
                        if(position>=total_num){
                            position=0;
                        }
                        play_music(idStr,nameStr,position);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarchaning = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarchaning = false;
                music.seekTo(seekBar.getProgress());//在当前位置播放
                now_info.setText(calculateTime(music.getCurrentPosition() / 1000));
            }
        });


    }

    //计算播放时间
    public String calculateTime(int time){
        int minute;
        int second;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
            //分钟再0~9
            if(minute >= 0 && minute < 10){
                //判断秒
                if(second >= 0 && second < 10){
                    return "0"+minute+":"+"0"+second;
                }else {
                    return "0"+minute+":"+second;
                }
            }else {
                //分钟大于10再判断秒
                if(second >= 0 && second < 10){
                    return minute+":"+"0"+second;
                }else {
                    return minute+":"+second;
                }
            }
        }else if(time < 60){
            second = time;
            if(second >= 0 && second < 10){
                return "00:"+"0"+second;
            }else {
                return "00:"+ second;
            }
        }
        return null;
    }

    void play_music(int[] idStr,String[] nameStr,int p_position){
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
        TextView song_info = findViewById(R.id.song_info);
        TextView now_info = findViewById(R.id.now_info);
        if(playMode){
            music.reset();
            playMode = false;
        }
        seekBar.setProgress(0);
        Log.d("IN", "进入播放分支");
        music.reset();
        music = MediaPlayer.create(context,idStr[p_position]);
        music.start();
        playMode = true;
        int progressTime = music.getDuration();
        seekBar.setMax(progressTime);
//            time_info.setText(Integer.toString(progressTime/1000)+ " s");

        now_info.setText(calculateTime(music.getCurrentPosition() / 1000));
        time_info.setText(calculateTime(progressTime/1000));
        song_info.setText(nameStr[p_position]);
        //图片设置
        setAlbum(nameStr,p_position);
        //背景设置
        setBackGroundColor();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isSeekBarchaning){
                    seekBar.setProgress(music.getCurrentPosition());
                }
            }
        },0,10);
    }


    private void setAlbum(String[] nameStr,int position){
        ImageView imageView = findViewById(R.id.album);
        imageView.setImageResource(getResources().getIdentifier(nameStr[position],"drawable",getPackageName()));
    }

    private void setBackGroundColor(){
        ConstraintLayout constraintLayout = findViewById(R.id.display);
        constraintLayout.setBackgroundColor(new Random().nextInt());
        //Log.d("TAG", "setBackGroundColor: " + randomHexString(6));
    }

    public void setLoopMode(View view) {
        Button loopButton =findViewById(R.id.loopType);
        ++loopMode;
        if(loopMode > 3){
            loopMode = 0;
        }
        if (loopMode == 0){
            loopButton.setText("播完停止");
            Log.d("TAG", "setLoopMode: 播完停止");
        }
        if(loopMode == 1){
            loopButton.setText("单曲循环");
            Log.d("TAG", "setLoopMode: 单曲循环");
        }
        if(loopMode == 2){
            loopButton.setText("顺序播放");
            Log.d("TAG", "setLoopMode: 顺序播放");
        }
        if(loopMode == 3){
            loopButton.setText("随机播放");
            Log.d("TAG", "setLoopMode: 随机播放");
        }
    }

    public void prev_onclick(View view) {
        TextView song_info = findViewById(R.id.song_info);
        playMode = false;
        music.reset();
        if (position - 1 < 0){
            song_info.setText("没有上一首歌曲");
            position = total_num;
        }
        play_music(idStr,nameStr,--position);
    }

    public void next_onclick(View view) {
        TextView song_info = findViewById(R.id.song_info);
        playMode = false;
        music.reset();
        if (position + 1 >= total_num){
            song_info.setText("没有下一首歌曲");
            position = -1;
        }
        play_music(idStr,nameStr,++position);
    }

    public void pause_onclick(View view) {
        Button playButton =findViewById(R.id.btn_pause);
        if(music.isPlaying()){
            music.pause();
            playButton.setText("播放");
        }
        else{
            music.start();
            playButton.setText("暂停");
        }
    }
}
