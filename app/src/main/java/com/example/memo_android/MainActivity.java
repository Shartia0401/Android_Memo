package com.example.memo_android;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
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
    FloatingActionButton fabMain;
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

                    if(fileSystem.checkfile(wr.title() + ".txt") && !wr.currentFile.getName().equals(wr.title() + ".txt")){
                        onClick_setting_costume_save();
                    }else{
                        if(!wr.currentFile.getName().equals(wr.title() + ".txt")){
                            fileSystem.fileDel(wr.currentFile.getName());
                        }
                        fileSystem.save(wr.title(),wr.content()); //TODO 세이브
                        wr.currentFile = fileSystem.openFile(wr.title());
                        if(dbHelper.InsertOrUpdate(wr.title())){
                            dbHelper.update(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                        }else{
                            dbHelper.insert(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                        }
                        Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                break;
            case R.id.item2:
                isModify = !isModify;
                toast.cancel();
                if(isModify){
                    toast.setText("수정모드 ON");
                }else{
                    toast.setText("수정모드 OFF");
                }

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
        fabMain = findViewById(R.id.fabMain);
        fabNew = findViewById(R.id.fabNew);
        fabOpen = findViewById(R.id.fabOpen);
        fabMain.setOnClickListener(v -> toggleFab());
        fabOpen.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();
            wr.et_title.setText("");
            wr.et_content.setText("");
            wr.isBold = false;
            wr.currentSize = wr.defaultsize;
            wr.currentFile = null;
            wr.setUI();
        });
        fabNew.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("path", null);
            bundle.putString("isBold", null);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            wr.setArguments(bundle);
            transaction.replace(R.id.frameLayout, wr);
            transaction.commit();
            try{
                wr.et_title.setText("");
                wr.et_content.setText("");
                wr.isBold = false;
                wr.currentSize = wr.defaultsize;
                wr.currentFile = null;
                wr.setUI();
            }catch (NullPointerException e){
                e.getStackTrace();
            }
        });
    }
    public void toggleFab(){
        if(fabMain_status){
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", 0f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", 0f);
            fs_animation.start();
            ObjectAnimator fm_anmation = ObjectAnimator.ofFloat(fabMain, "rotation", 0 );

            fm_anmation.start();

        } else{
            ObjectAnimator fn_animation = ObjectAnimator.ofFloat(fabNew, "translationY", -200f);
            fn_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabOpen, "translationY", -400f);
            fs_animation.start();
            ObjectAnimator fm_anmation = ObjectAnimator.ofFloat(fabMain, "rotation", 135 );
            fm_anmation.start();
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
            cursor.moveToFirst();
            bundle.putString("isBold", cursor.getString(0));
            bundle.putString("size", cursor.getString(1));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        wr.setArguments(bundle);
        transaction.replace(R.id.frameLayout, wr);
        transaction.commit();
    }
    private void onClick_setting_costume_save(){
        new AlertDialog.Builder(this)
                .setTitle("파일 덮어쓰기")
                .setMessage("동일한 파일이 두개 이상 있습니다. 덮어쓰기 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        fileSystem.fileDel(wr.title() + ".txt");

                        if(!wr.currentFile.getName().equals(wr.title() + ".txt")){
                            fileSystem.fileDel(wr.currentFile.getName());
                        }
                        fileSystem.save(wr.title(),wr.content()); //TODO 세이브
                        wr.currentFile = fileSystem.openFile(wr.title());
                        if(dbHelper.InsertOrUpdate(wr.title())){
                            dbHelper.update(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                        }else{
                            dbHelper.insert(wr.title(), wr.isBold(), Integer.toString(wr.currentSize));
                        }
                        Toast.makeText(getApplicationContext(), "저장", Toast.LENGTH_SHORT).show();
                    }})
                .show();
    }
}