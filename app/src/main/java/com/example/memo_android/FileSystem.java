package com.example.memo_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileSystem {

    ArrayList<String> txtList;



    public FileSystem(){
        txtList = new ArrayList<>();
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
                }
            }catch (NullPointerException e){
                e.getStackTrace();
            }
        }
        else{
            Log.d("bug", "파일 시스템");
        }
    }

    public File openFile(String path){
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"+path;
            return new File(filepath);
    }

    public File[]  isFileExistsCheck(){ //TODO 파일 리스트
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File dir = new File(filePath);
        FileFilter filter = f -> f.getName().endsWith("txt");
        File[] files = dir.listFiles(filter);
        assert files != null;
        for (File file : files) {
            Log.d("test", file.toString() + "파일");
        }
        return files;
    }

    public void openContent(File txt){

        txtList = new ArrayList<>();
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

    public void fileDel(String path){

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + path;

        try{
            File file = new File(filePath);

            if(file.exists()){
                file.delete();
            }
        }catch(Exception e){
            e.getStackTrace();
        }
    }

    public boolean checkfile(String path){
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + path;

        try{
            File file = new File(filePath);

            if(file.exists()){
                return true;
            }
        }catch(Exception e){
            e.getStackTrace();
        }
        return false;
    }

    public ArrayList<String> listPass(){
        return txtList;
    }
}