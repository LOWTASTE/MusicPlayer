package com.example.musicplayer;
import android.content.Context;
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
    int currentPosition;
    int position;
    boolean playMode;
    boolean isSeekBarchaning;
    static int loopMode = 0;
    static int total_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 基本操作
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView time_info = findViewById(R.id.time_info);
        TextView song_info = findViewById(R.id.song_info);
        TextView now_info = findViewById(R.id.now_info);
        Button prev = findViewById(R.id.btn_prev);
        Button pause = findViewById(R.id.btn_pause);
        Button next = findViewById(R.id.btn_next);
        Objects.requireNonNull(getSupportActionBar()).hide();


//        imageView.setImageResource(R.drawable.alliaskofyou);

        //获取传入信息
        currentPosition = getIntent().getIntExtra("currentPosition", -1);
        position = getIntent().getIntExtra("position", -1);
        String[] nameStr =  getIntent().getStringArrayExtra("nameStr");
        int[] idStr = getIntent().getIntArrayExtra("idStr");
        playMode = getIntent().getBooleanExtra("playMode", false);
        total_num = getIntent().getIntExtra("total_num",0);

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
                playMode = true;
                music.reset();
                if (position - 1 < 0){
                    song_info.setText("没有上一首歌曲");
                    position = total_num;
                }
                play_music(idStr,nameStr,--position);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMode = true;
                music.reset();
                if (position + 1 >= total_num){
                    song_info.setText("没有下一首歌曲");
                    position = -1;
                }
                play_music(idStr,nameStr,++position);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration2 = music.getDuration() / 1000;//获取音乐总时长
                int position = music.getCurrentPosition();//获取当前播放的位置
                now_info.setText(calculateTime(position / 1000));//开始时间
                time_info.setText(calculateTime(duration2));//总时长
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
        Button next = findViewById(R.id.btn_next);
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
        setAlbum(idStr,nameStr,p_position);
        setBackGroundColor();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isSeekBarchaning){
                    seekBar.setProgress(music.getCurrentPosition());
                }
                if (!music.isPlaying()){
                    playMode = false;
                    Log.d("TAG", "playMode = false");
                    runLoop(idStr, nameStr);
//                        if (loopMode == 3){
//                            int jump = (int) (Math.random() * total_num);
//                            for (int i = 0;i < jump;i++){
//                                next.performClick();
//                            }
//
//                        }


                }
            }
        },0,50);
    }

    private void initView(){
        TextView now_info = findViewById(R.id.now_info);
        TextView time_info = findViewById(R.id.time_info);
        SeekBar seekBar = findViewById(R.id.seekBar);
        //绑定监听器，监听拖动到指定位置
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration = music.getDuration() / 1000;//获取音乐总时长
                int position = music.getCurrentPosition();//获取当前播放的位置
                now_info.setText(calculateTime(position / 1000));//开始时间
                time_info.setText(calculateTime(duration));//总时长
            }
            /*
             * 通知用户已经开始一个触摸拖动手势。
             * */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarchaning = true;
            }
            /*
             * 当手停止拖动进度条时执行该方法
             * 首先获取拖拽进度
             * 将进度对应设置给MediaPlayer
             * */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarchaning = false;
                music.seekTo(seekBar.getProgress());//在当前位置播放
                now_info.setText(calculateTime(music.getCurrentPosition() / 1000));
            }
        });
    }

    private void setAlbum(int[] idStr,String[] nameStr,int position){
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
        }
    }


    public void runLoop(int[] idStr,String[] nameStr) {
//        Button loopButton =findViewById(R.id.loopType);
        Button next = findViewById(R.id.btn_next);
        ++loopMode;
        if(loopMode > 3){
            loopMode = 0;
        }
        if (loopMode == 0){
            return;
        }
        if(loopMode == 1){
            play_music(idStr, nameStr, position);
        }
        if(loopMode == 2){
            next.performClick();
        }
        if(loopMode == 3){

        }
    }
}
