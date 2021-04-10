package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {
        private LyricView lyricView;
        private MediaPlayer mediaPlayer;
        private Button button;
        private SeekBar seekBar;
        private String mp3Path;
        private int INTERVAL=45;//歌词每行的间隔

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.test_lyc);
            Context context = TestActivity.this;

            //mp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LyricSync/1.mp3";

            lyricView = (LyricView) findViewById(R.id.my_lrc);
            // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //开始播放
            mediaPlayer = MediaPlayer.create(context,R.raw.alliaskofyou);
            mediaPlayer.start();
            //ResetMusic(mp3Path);
            //todo ： 得到歌词路径
            //SerchLrc();

//            String lrc = Objects.requireNonNull(this.getClass().getResource("/main/res/lyrics/All I Ask Of You.lrc")).getPath();
            String lrc = "";
            Log.d("TAG", lrc);
            LyricView.read(lrc);
            lyricView.SetTextSize();
            lyricView.setOffsetY(350);

            lyricView.SetTextSize();

            button = (Button) findViewById(R.id.button);
            button.setText("播放");

            //回调函数
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //ResetMusic(mp3Path);
                    lyricView.SetTextSize();
                    lyricView.setOffsetY(200);
                    mediaPlayer.start();
                }
            });
//            seekBar.setMax(mediaPlayer.getDuration());
            new Thread(new runable()).start();
        }

//        public void SerchLrc() {
//            String lrc = mp3Path;
//            lrc = lrc.substring(0, lrc.length() - 4).trim() + ".lrc".trim();
//            LyricView.read(lrc);
//            lyricView.SetTextSize();
//            lyricView.setOffsetY(350);
//        }

//        public void ResetMusic(String path) {
//
//            mediaPlayer.reset();
//            try {
//
//                mediaPlayer.setDataSource(mp3Path);
//                mediaPlayer.prepare();
//            } catch (IllegalArgumentException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        class runable implements Runnable {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {

                    try {
                        Thread.sleep(100);
                        if (mediaPlayer.isPlaying()) {
                            lyricView.setOffsetY(lyricView.getOffsetY() - lyricView.SpeedLrc());
                            lyricView.SelectIndex(mediaPlayer.getCurrentPosition());
                            //seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            mHandler.post(mUpdateResults);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        Handler mHandler = new Handler();
        Runnable mUpdateResults = new Runnable() {
            public void run() {
                lyricView.invalidate(); // 更新视图
            }
        };
}
