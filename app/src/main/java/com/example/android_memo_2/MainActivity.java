package com.example.android_memo_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean fabMain_status = false;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabNew;
    private FloatingActionButton fabOpen;

    writeFragment wrfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrfragment = new writeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, wrfragment).commit();

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
            }
        });
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "새로만들기", Toast.LENGTH_SHORT).show();
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


}