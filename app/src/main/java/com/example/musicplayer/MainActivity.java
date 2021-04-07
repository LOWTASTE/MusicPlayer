package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    //播放状态
    static boolean playMode = false;
    static int currentPosition = -1;
    //MediaPlayer music = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = MainActivity.this;
        List<HashMap<String, String>> list = new ArrayList<>();
        ListView listView=findViewById(R.id.list_view);


        //隐藏上部标题
        Objects.requireNonNull(getSupportActionBar()).hide();
        //读取文件
        Field[] fields = R.raw.class.getDeclaredFields();
        String[] nameStr=new String[fields.length];
        //todo : 获取歌曲信息
        int[] idStr = new int[fields.length];
        int[] infoStr = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            nameStr[i] = fields[i].getName();
            idStr[i] = getResources().getIdentifier(nameStr[i],"raw",getPackageName());
        }


        //装入hash_map用于显示信息
        for (int i = 1;i<=fields.length;i++){
            String str = Integer.valueOf(i).toString();
            HashMap<String,String> map=new  HashMap<String, String> ();
            map.put("id",str);
            map.put("name",nameStr[i-1]);
            map.put("info"," ");
            list.add(map);
        }
        // ListView设置适配器
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item_songs,
                new String[] { "id","name","info" }, new int[] { R.id.txt_id,R.id.song_name,R.id.txt_info });
        listView.setAdapter(adapter);

        //播放逻辑
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    music.reset();
//                    music = MediaPlayer.create(context,idStr[position]);
//                    music.start();
//                    playMode = true;
//                    currentPosition = position;
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,DisplayActivity.class);
                    intent.putExtra("nameStr", nameStr);
                    intent.putExtra("idStr",idStr);
//                    intent.putExtra("time",music.getDuration());
                    intent.putExtra("currentPosition",currentPosition);
                    intent.putExtra("position",position);
                    intent.putExtra("playMode",playMode);
                    startActivity(intent);
               // setContentView(R.layout.display_layout);
            }
        });


    }
}