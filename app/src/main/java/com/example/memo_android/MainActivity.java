package com.example.memo_android;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private boolean fabMain_status = false;
    WriteFragment wr;
    MainFragment mf;
    FloatingActionButton fabNew;
    FloatingActionButton fabOpen;
    FileSystem fileSystem;

    DB_Helper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFab();
        setFm();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }
    @SuppressLint({"NonConstantResurceId", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if(fragment instanceof WriteFragment){
            if (item.getItemId() == R.id.item1) {
                Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();
                fileSystem.save(wr.title(),wr.content()); //TODO 세이브
                dbHelper.insert(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void setFm(){
        wr = new WriteFragment();
        mf = new MainFragment();
        fileSystem = new FileSystem();
        dbHelper = new DB_Helper(getApplicationContext());
    }

    public void setFab(){
        FloatingActionButton fabMain = findViewById(R.id.fabMain);
        fabNew = findViewById(R.id.fabNew);
        fabOpen = findViewById(R.id.fabOpen);
        fabMain.setOnClickListener(v -> toggleFab());
        fabOpen.setOnClickListener(v -> {getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();});
        fabNew.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, wr).commit();
        });
    }
    public void toggleFab(){
        if(fabMain_status){
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", 0f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", 0f);
            fs_animation.start();
        } else{
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", -200f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", -400f);
            fs_animation.start();
        }
        fabMain_status = !fabMain_status;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 시스템 Back 버튼 호출 시
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if(fragment instanceof WriteFragment) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();
                return true;
            }
        }else{
            return super.onKeyDown(keyCode, event); // 코드 제거 시 뒤로가기 기능이 수행되지 않음
        }
        return true;
    }
}