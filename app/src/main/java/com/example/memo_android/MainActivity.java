package com.example.memo_android;

import android.animation.ObjectAnimator;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private boolean fabMain_status = false;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabNew;
    private FloatingActionButton fabOpen;
    WriteFragment wr;
    MainFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wr = new WriteFragment();
        mf = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();

        fabMain = findViewById(R.id.fabMain);
        fabNew = findViewById(R.id.fabNew);
        fabOpen = findViewById(R.id.fabOpen);

        fabMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                toggleFab();
            }
        });

        fabOpen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "저장", Toast.LENGTH_SHORT).show();
                save(wr.title(),wr.content());
            }
        });
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "새로만들기", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, wr).commit();
                wr.newtext();
                isFileExistsCheck();
            }
        });
    }

    public void toggleFab(){
        if(fabMain_status){
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", 0f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", 0f);
            fs_animation.start();
//            fabMain.setImageResource(R.drawable.ic_);
        } else{
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", -200f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", -400f);
            fs_animation.start();
//            fabMain.setImageResource(R.drawab)
        }

        fabMain_status = !fabMain_status;

    }

    public void save(String title, String content){ //TODO 파일 저장

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), title + ".txt");
            try{
                FileWriter fw = new FileWriter(file, false);
                fw.write(content);
                fw.close();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
        }
    }

    public void  isFileExistsCheck(){ //TODO 파일 리스트
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File dir = new File(filePath);
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith("txt");
            }
        };
        File files[] = dir.listFiles(filter);
        for (File file : files) {
            Log.d("test", file.toString() + "파일");
        }
    }
}