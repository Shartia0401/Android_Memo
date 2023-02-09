package com.example.memo_android;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.database.Cursor;
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
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements SendPath{

    private boolean fabMain_status = false;
    WriteFragment wr;
    MainFragment mf;
    FloatingActionButton fabNew;
    FloatingActionButton fabOpen;
    FileSystem fileSystem;

    DB_Helper dbHelper;

    Toast toast;

    boolean isModify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setFm();
        setFab();
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

        switch (item.getItemId()) {
            case R.id.item1:
                if(fragment instanceof WriteFragment){
                    Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();
                    fileSystem.save(wr.title(),wr.content()); //TODO 세이브
                    if(dbHelper.InsertOrUpdate(wr.title())){
                        dbHelper.update(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                    }else{
                        dbHelper.insert(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                    }
                    return true;
                }
                break;
            case R.id.item2:
                isModify = !isModify;
                toast.cancel();
                toast.setText(Boolean.toString(isModify));
                toast.show();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isModify", isModify);
                mf.setArguments(bundle);
                break;
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
            Bundle bundle = new Bundle();
            bundle.putString("path", null);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            wr.setArguments(bundle);
            transaction.replace(R.id.frameLayout, wr);
            transaction.commit();
            wr.et_title.setText("");
            wr.et_content.setText("");
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

    @Override
    public void sendPath(String s) {
        Bundle bundle = new Bundle();

        Toast.makeText(this, "path:" + s, Toast.LENGTH_SHORT).show();
        bundle.putString("path", s);
        if(dbHelper.InsertOrUpdate(s.substring(0, s.length()-4))){
            Cursor cursor = dbHelper.getUserList(s.substring(0, s.length()-4));
            bundle.putString("isBold", cursor.getString(0));
            bundle.putString("size", cursor.getString(1));
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        wr.setArguments(bundle);
        transaction.replace(R.id.frameLayout, wr);
        transaction.commit();
    }
}