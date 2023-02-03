package com.example.memo_android;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {


    private boolean fabMain_status = false;
    private FloatingActionButton fabNew;
    private FloatingActionButton fabOpen;

    private ArrayList<String> txtList;
    private String filename;
    WriteFragment wr;
    MainFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setFab();
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
                save(wr.title(),wr.content());
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFab(){
        wr = new WriteFragment();
        mf = new MainFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();

        FloatingActionButton fabMain = findViewById(R.id.fabMain);
        fabNew = findViewById(R.id.fabNew);
        fabOpen = findViewById(R.id.fabOpen);

        fabMain.setOnClickListener(v -> toggleFab());

        fabOpen.setOnClickListener(v -> {getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mf).commit();});
        fabNew.setOnClickListener(view -> newPage());
    }

    public void newPage(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, wr).commit();
        wr.newText();
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


    public void openFile(String path){
        txtList = new ArrayList<>();
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"+path;
        File txt = new File(filepath);
        filename = txt.getName();

        try{
            BufferedReader br = new BufferedReader(new FileReader(txt));
            String str = br.readLine();
            while(str !=null){
                txtList.add(str);
                str = br.readLine();
            }

            br.close();
        }catch(IOException e){
            e.getStackTrace();
            Log.d("TEST", e.toString());
        }catch (NullPointerException e){
            Log.d("TEST", "null");
        }
    }

    public ArrayList<String> listPass(){
        return txtList;
    }
    public String namePass(){
        return filename.substring(0, filename.length()-4);
    }

    public void save(String title, String content){ //TODO 파일 저장
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

           try{
               File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), title + ".txt");
               try{
                   FileWriter fw = new FileWriter(file, false);
                   fw.write(content);
                   fw.close();
               } catch (IOException e){
                   e.printStackTrace();
                   Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
               }
           }catch (NullPointerException e){
               e.getStackTrace();
           }
        }
        else{
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
        }
    }

    public File[]  isFileExistsCheck(){ //TODO 파일 리스트
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File dir = new File(filePath);
        FileFilter filter = f -> f.getName().endsWith("txt");
        File[] files = dir.listFiles(filter);
        assert files != null;
        for (File file :    files) {
            Log.d("test", file.toString() + "파일");
        }
        return files;
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